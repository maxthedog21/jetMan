package com.example.jetman

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.example.jetman.ui.theme.JetManTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetManTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var context = LocalContext.current;
                    var configuration = LocalConfiguration.current;
                    var midX = configuration.screenWidthDp / 2 + (configuration.screenWidthDp * 0.50);
                    var midY = configuration.screenWidthDp / 2 + (configuration.screenHeightDp * 0.50);
                    val infiniteTransition = rememberInfiniteTransition();

                        val frame by infiniteTransition.animateValue(
                            initialValue = 0,
                            targetValue = 6,
                            typeConverter = Int.VectorConverter,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart,
                            )
                        )
                    Column(Modifier.padding(innerPadding).background(Color.Black)) {
                    Text("assets/hero/Sprite/Attack${frame}.png")
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawHero(frame, Offset(midX.toFloat(), midY.toFloat()), context, configuration)
                    }
                    }

                }
            }
        }
    }
}
fun DrawScope.drawHero(frame: Int, offset: Offset, context: Context, configuration: Configuration){
    val drawInts: Array<Int> = arrayOf(R.drawable.attack_one0, R.drawable.attack_one1, R.drawable.attack_one2, R.drawable.attack_one3, R.drawable.attack_one4, R.drawable.attack_one5, R.drawable.attack_one6);
    var imageBitmap: ImageBitmap = loadImage(
        drawInts[frame],
        context,
        configuration
    );
    drawImage(imageBitmap, offset);
}
fun loadImage(frameInt: Int, context: Context, configuration: Configuration): ImageBitmap{

   var drawMap: Drawable?  = context.getDrawable(frameInt);
    if(drawMap != null){
        var width: Double =  configuration.screenWidthDp * 0.50;
        var height: Double =  configuration.screenHeightDp * 0.50;
      return  drawMap.toBitmap().scale(width.toInt(), height.toInt()).asImageBitmap();
    }
    return ImageBitmap(50, 50);
}


