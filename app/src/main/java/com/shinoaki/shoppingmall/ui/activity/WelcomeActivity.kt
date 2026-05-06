package com.shinoaki.shoppingmall.ui.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.databinding.ActivityWelcomeBinding
import com.shinoaki.shoppingmall.ui.activity.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeActivity : AppCompatActivity() {

    private lateinit var userDataStoreManager: LoginDataStoreManager
    lateinit var binding: ActivityWelcomeBinding

    private var countdownTimer: CountDownTimer? = null
    private val images = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.avatar4
    )

    private var imageAnimator: ValueAnimator? = null
    private var bounceAnimator: ValueAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataStoreManager = LoginDataStoreManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 开始动画序列
        startSynchronizedAnimation()
        applyBlurToBackground()
        startCountdown(5)
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStoreManager.isLoggedIn()
                .take(1).collect { isLoggedIn ->
                    withContext(Dispatchers.Main) {
                        if (isLoggedIn) {
                            // 已登录，跳转主页
                            startActivity(
                                Intent(
                                    this@WelcomeActivity,
                                    NavigationBarActivity::class.java
                                )
                            )
                            finishAffinity()
                        } else {
                            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
        }
    }

    private fun applyBlurToBackground() {
        // 如果需要背景模糊，可以使用 RenderScript
        // 或者使用 Glide 加载时添加模糊效果
//        Glide.with(this)
//            .load(R.drawable.arona_37)
//            .centerCrop()
//            .transform(BlurTransformation(10)) // 需要添加 glide-transformations 库
//            .into(binding.bgImage)
    }

    /**
     * 启动同步动画
     * @param durationMs 动画时长（毫秒），传入0表示无限循环
     * @param imageSwitchIntervalMs 图片切换间隔（毫秒），默认5000ms
     * @param bounceDurationMs 跳动动画单次时长（毫秒），默认1250ms
     */
    private fun startSynchronizedAnimation(
        durationMs: Long = 0,
        imageSwitchIntervalMs: Long = 5000,
        bounceDurationMs: Long = 1250
    ) {
        // 图片切换动画
        val imageRepeatMode = if (durationMs == 0L) ValueAnimator.INFINITE else 1
        val imageTotalDuration = if (durationMs == 0L) imageSwitchIntervalMs else durationMs

        imageAnimator = ValueAnimator.ofFloat(0f, 4f).apply {
            duration = imageTotalDuration
            repeatCount = imageRepeatMode
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                val index = (value % 4).toInt()

                if (index != getCurrentImageIndex()) {
                    binding.ivAvatar.setImageResource(images[index])
                }
            }
            start()
        }

        // 跳动动画
        val bounceRepeatMode = if (durationMs == 0L) ValueAnimator.INFINITE else 1
        val bounceTotalDuration = if (durationMs == 0L) bounceDurationMs else durationMs

        bounceAnimator = ValueAnimator.ofFloat(0f, 60f, 0f).apply {
            duration = bounceTotalDuration
            repeatCount = bounceRepeatMode
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { animation ->
                val y = animation.animatedValue as Float
                binding.ivAvatar.translationY = y
            }
            start()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getCurrentImageIndex(): Int {
        val drawable = binding.ivAvatar.drawable
        images.forEachIndexed { index, res ->
            if (drawable.constantState == resources.getDrawable(res, null).constantState) {
                return index
            }
        }
        return -1
    }

    private fun startCountdown(seconds: Int) {
        countdownTimer = object : CountDownTimer((seconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = (millisUntilFinished / 1000).toInt()
                binding.tvCountdown.text = remainingSeconds.toString()
            }

            override fun onFinish() {
                binding.tvCountdown.text = "0"
                binding.tvCountdown.visibility = View.GONE
                // 倒计时结束后的逻辑，比如跳转页面
                checkLoginStatus()
            }
        }.start()
    }

    private fun stopAnimation() {
        imageAnimator?.cancel()
        bounceAnimator?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimation()
    }
}