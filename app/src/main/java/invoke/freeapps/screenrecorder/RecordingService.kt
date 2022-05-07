package invoke.freeapps.screenrecorder

import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.core.view.updateLayoutParams
import kotlin.math.roundToInt

class RecordingService : Service() {

    private var motionX: Float = 0F
    private var motionY: Float = 0F
    private var startX: Int = 0
    private var startY: Int = 0
    private var canMove: Boolean = false
    private lateinit var mRecordBtn:ImageButton
    private lateinit var mSettingsBtn:ImageButton
    private lateinit var mCloseBtn:ImageButton

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        println("Service Created!")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("Service Started!")
        var mLayoutInflater: LayoutInflater =
            getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var mView: View = mLayoutInflater.inflate(R.layout.overlay_layout, null)
        var mWindowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        var mLayoutParams: WindowManager.LayoutParams =
            WindowManager.LayoutParams(
                // Shrink the window to wrap the content rather
                // than filling the screen
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                // Display it on top of other application windows
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                // Don't let it grab the input focus
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                // Make the underlying application window visible
                // through any transparent parts
                PixelFormat.TRANSLUCENT

            )
        mCloseBtn = mView.findViewById(R.id.close_button)
        mSettingsBtn = mView.findViewById(R.id.settings_button)
        mRecordBtn = mView.findViewById(R.id.record_button)
        mLayoutParams.x = 100
        mLayoutParams.y = 200
        mLayoutParams.gravity = Gravity.CENTER
        var buttons = listOf<View>(
            mRecordBtn,mSettingsBtn,mCloseBtn
        )
        mRecordBtn.setOnClickListener {
            println("StartRecording!")
        }
        mSettingsBtn.setOnClickListener {
            var mIntent:Intent = Intent(this, MainActivity::class.java)
            mIntent.flags=FLAG_ACTIVITY_NEW_TASK
            startActivity(mIntent)
        }
        mCloseBtn.setOnClickListener {
            stopSelf()
            mWindowManager.removeView(mView)
        }
        buttons.forEach {
            it.setOnTouchListener { view, motionEvent ->

                var oldParams: WindowManager.LayoutParams = mLayoutParams
                if (motionEvent.action == MotionEvent.ACTION_MOVE && canMove) {

                    oldParams.x = (motionEvent.rawX - motionX + startX).toInt()
                    oldParams.y = (motionEvent.rawY - motionY + startY).toInt()
                    mWindowManager.updateViewLayout(mView, oldParams)
                } else if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    startX = mLayoutParams.x
                    startY = mLayoutParams.y
                    motionX = motionEvent.rawX
                    motionY = motionEvent.rawY
                }else if (motionEvent.action == MotionEvent.ACTION_UP && canMove) {
                    canMove = false
                    startX = 0
                    startY = 0
                    motionX = 0F
                    motionY = 0F
                } else if(motionEvent.action == MotionEvent.ACTION_UP && !canMove){
                    view.performClick()
                    return@setOnTouchListener true
                }
                return@setOnTouchListener false
            }
        }



        buttons.forEach {
            it.setOnLongClickListener {
                startX = mLayoutParams.x
                startY = mLayoutParams.y
                canMove = true
                return@setOnLongClickListener true
            }
        }
        mWindowManager.addView(mView, mLayoutParams)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        println("Service Destroyed!")
        super.onDestroy()
    }
}