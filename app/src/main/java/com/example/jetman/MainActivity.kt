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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.example.jetman.ui.theme.JetManTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetManTheme {

                val currentAction: MutableState<SpriteAction> = remember { mutableStateOf(SpriteAction.IDLE) }
               // val sliderValue: MutableState<Float> = remember { mutableStateOf(1f) }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar =  {
                        TopAppBar(
                            title={
                                Column(){

                                    if(currentAction.value == SpriteAction.IDLE){
                                        Text("Idle Animation" )
                                    }else if(currentAction.value == SpriteAction.RUN){
                                        Text("Run Animation")
                                    }else{
                                        Text("Attack Animation")
                                    }
                                  /*  Slider(value = sliderValue.value, onValueChange = {
                                        sliderValue.value = it
                                    })*/
                                }
                            }
                        )


                    },
                    bottomBar = {
                        BottomAppBar() {

                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                ) {
                                    Button(onClick = {
                                        currentAction.value = SpriteAction.RUN;
                                    }) {
                                        Text("Run")
                                    }
                                    Button(onClick = {
                                        currentAction.value = SpriteAction.IDLE;
                                    }) {
                                        Text("Idle")
                                    }
                                    Button(onClick = {
                                        currentAction.value = SpriteAction.ATTACK;
                                    }) {
                                        Text("Attack")
                                    }

                                }

                        }
                    }
                    ) { innerPadding ->
                    val context = LocalContext.current;
                    val configuration = LocalConfiguration.current;
                    val midX = configuration.screenWidthDp / 2 + (configuration.screenWidthDp * 0.50);
                    val midY = configuration.screenWidthDp / 2 + (configuration.screenHeightDp * 0.50);
                    val actionData: HashMap<SpriteAction, Array<Int>> = getActionData();
                    val infiniteTransition = rememberInfiniteTransition();

                        val frame by infiniteTransition.animateValue(
                            initialValue = 0,
                            targetValue = actionData[currentAction.value]!!.size,
                            typeConverter = Int.VectorConverter,
                            animationSpec = infiniteRepeatable(
                                animation = tween(600, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart,
                            )
                        )
                            Canvas(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color.Black)) {
                                drawHero(
                                    frame,
                                    actionData[currentAction.value]!!,
                                    Offset(midX.toFloat(), midY.toFloat()),
                                    context,
                                    configuration
                                )
                            }
                    }

                }
            }
        }
    }
fun DrawScope.drawHero(frame: Int, drawInts: Array<Int>, offset: Offset, context: Context, configuration: Configuration){
    val imageBitmap: ImageBitmap = loadImage(
        drawInts[frame],
        context,
        configuration
    );
    drawImage(imageBitmap, offset);
}
fun loadImage(frameInt: Int, context: Context, configuration: Configuration): ImageBitmap{

   val drawMap: Drawable?  = context.getDrawable(frameInt);
    if(drawMap != null){
        val width: Double =  configuration.screenWidthDp * 0.50;
        val height: Double =  configuration.screenHeightDp * 0.50;
      return  drawMap.toBitmap().scale(width.toInt(), height.toInt()).asImageBitmap();
    }
    return ImageBitmap(50, 50);
}
fun getActionData(): HashMap<SpriteAction, Array<Int>>{
    val map: HashMap<SpriteAction, Array<Int>> = HashMap<SpriteAction, Array<Int>>();
    map.put(
        SpriteAction.RUN,
        arrayOf(
            R.drawable.herorun0, R.drawable.herorun1, R.drawable.herorun2, R.drawable.herorun3, R.drawable.herorun4,
        )
    )
    map.put(
        SpriteAction.IDLE,
        arrayOf(
            R.drawable.heroidle0, R.drawable.heroidle1, R.drawable.heroidle2, R.drawable.heroidle3, R.drawable.heroidle4, R.drawable.heroidle5, R.drawable.heroidle6, R.drawable.heroidle7, R.drawable.heroidle8, R.drawable.heroidle9
        )
    )
    map.put(
        SpriteAction.ATTACK,
        arrayOf(
            R.drawable.attack_one0, R.drawable.attack_one1, R.drawable.attack_one2, R.drawable.attack_one3, R.drawable.attack_one4, R.drawable.attack_one5, R.drawable.attack_one6
        )
    )


    return map;
}


