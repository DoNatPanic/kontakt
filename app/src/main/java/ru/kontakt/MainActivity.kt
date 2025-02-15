package ru.kontakt

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.vk.api.sdk.VK
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.vksdksupport.withVKIDToken
import com.vk.sdk.api.account.AccountService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kontakt.data.network.EnumConverterFactory
import ru.kontakt.data.vk.VkAPI
import ru.kontakt.data.vk.dto.NewsFeedFilterDto
import ru.kontakt.databinding.ActivityMainBinding
import ru.kontakt.ui.profile.ProfilePageState


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val state = MutableLiveData<ProfilePageState>(ProfilePageState.Unauthorized)
    private lateinit var vkClient: VkAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        VKID.init(this)
        VK.initialize(this)

        vkClient = Retrofit.Builder()
            .baseUrl("https://api.vk.ru/method/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(EnumConverterFactory())
            .build()
            .create(VkAPI::class.java)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.vkLogin.setCallbacks(
            onAuth = { oAuth, token -> onLogin(oAuth, token) },
            onFail = { oAuth, fail ->
                Log.w("vkLogin", "login failed")
            }
        )
        binding.logoutButton.setOnClickListener { logout() }

        state.observe(this) { state -> render(state) }

        if (VKID.instance.accessToken != null) {
            requestNewsFeed()
            requestAccountInfoAndRender()
        } else {
            state.postValue(ProfilePageState.Unauthorized)
        }
    }

    private fun render(state: ProfilePageState) {
        when (state) {
            is ProfilePageState.Unauthorized -> {
                binding.vkLogin.visibility = View.VISIBLE
                binding.logoutButton.visibility = View.GONE
                binding.profileFullName.visibility = View.GONE
                binding.profilePhoto.visibility = View.GONE
            }

            is ProfilePageState.LoggedIn -> {
                binding.vkLogin.visibility = View.GONE
                binding.logoutButton.visibility = View.VISIBLE
                binding.profileFullName.visibility = View.VISIBLE
                binding.profileFullName.text = state.fullName
                binding.profilePhoto.visibility = View.VISIBLE

                Glide.with(this)
                    .load(state.profilePhotoUrl)
                    .into(binding.profilePhoto)
            }
        }
    }

    private fun requestAccountInfoAndRender() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = VK.executeSync(
                AccountService()
                    .accountGetProfileInfo()
                    .withVKIDToken()
            )
            val fullName = "${result.firstName} ${result.lastName}"
            state.postValue(ProfilePageState.LoggedIn(fullName, result.photo200))
        }
    }

    private fun requestNewsFeed() {
        lifecycleScope.launch(Dispatchers.IO) {
            val auth = "Bearer ${VKID.instance.accessToken!!.token}"
            val feed = vkClient.getNewsFeed(NewsFeedFilterDto.POST, auth, count = 5)
            val anotherFeed =
                vkClient.getNewsFeed(null, auth, count = 5, startFrom = feed.response!!.nextFrom)
        }
    }

    private fun onLogin(oAuth: OneTapOAuth?, token: AccessToken) {
        requestNewsFeed()
        requestAccountInfoAndRender()
    }

    private fun logout() {
        lifecycleScope.launch(Dispatchers.IO) {
            VKID.instance.logout(callback = object : VKIDLogoutCallback {
                override fun onSuccess() {
                    state.postValue(ProfilePageState.Unauthorized)
                }

                override fun onFail(fail: VKIDLogoutFail) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}