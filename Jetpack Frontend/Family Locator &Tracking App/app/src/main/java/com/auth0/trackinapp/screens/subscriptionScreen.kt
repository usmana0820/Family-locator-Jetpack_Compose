package com.auth0.trackinapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.auth0.trackinapp.R
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Path
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.sp


@Composable
fun SubscriptionScreen(navController: NavController) {
    val blueishColor = Color(0xFF0098C3)
    val lightBlue = Color(0xFFE0F7FA)
    val whiteColor = Color.White
    val softBackground = Color(0xFFF4F9FC)

    var isVisible by remember { mutableStateOf(false) }
    val buttonOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Button Slide Animation"
    )

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Surface(modifier = Modifier.fillMaxSize(), color = softBackground) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Gradient Header with Wave
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(blueishColor, lightBlue)
                        )
                    )
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply {
                        moveTo(0f, size.height * 0.75f)
                        cubicTo(size.width * 0.3f, size.height, size.width * 0.7f, size.height * 0.5f, size.width, size.height * 0.75f)
                        lineTo(size.width, 0f)
                        lineTo(0f, 0f)
                        close()
                    }
                    drawPath(path, color = blueishColor)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Unlock Full Access",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = whiteColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Manage unlimited groups & members with Premium",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = whiteColor.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Free Plan Section
            SectionHeader("Free Plan",colorResource(id = R.color.blueish) )
            PlanCard(
                title1 = "Create up to 2 groups",
                title2 = "Add up to 3 members per group",
                icon1 = R.drawable.ic_group,
                icon2 = R.drawable.ic_person,
                locked = true,
                cardColor = whiteColor,
                iconColor = Color.Gray
            )
//            DescriptionText("Best for trying basic features of the Family Locator App.")

            // Upgrade Now Button
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = buttonOffset)
                    .padding(horizontal = 32.dp)
            ) {
                Button(
                    onClick = { /* Upgrade logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish)),
                    shape = RoundedCornerShape(40.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text(
                        text = "Upgrade Now",
                        color = whiteColor,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Premium Plan Section
            Spacer(modifier = Modifier.height(32.dp))
            SectionHeader("Premium Plan ✅", colorResource(id = R.color.blueish))
            PlanCard(
                title1 = "Create unlimited groups",
                title2 = "Add up to 30 members per group",
                icon1 = R.drawable.ic_group,
                icon2 = R.drawable.ic_person,
                locked = false,
                cardColor = lightBlue.copy(alpha = 0.3f),
                iconColor = colorResource(id = R.color.blueish)
            )
            DescriptionText("Perfect for large families and full-feature access including real-time location sharing and group management.")
        }
    }
}

@Composable
fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 20.dp),
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = color
        )
    )
}

@Composable
fun DescriptionText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
        textAlign = TextAlign.Center
    )
}

@Composable
fun PlanCard(
    title1: String,
    title2: String,
    icon1: Int,
    icon2: Int,
    locked: Boolean,
    cardColor: Color,
    iconColor: Color
) {
    val isPremium = !locked

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = if (isPremium)
                    Brush.horizontalGradient(listOf(Color(0xFFB2EBF2), Color(0xFFE0F7FA)))
                else SolidColor(cardColor),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if (isPremium) 2.dp else 0.dp,
                color = if (isPremium) iconColor else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isPremium) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "★ Premium",
                        color = iconColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(iconColor.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlanFeatureColumn(iconId = icon1, label = "Groups", value = title1, locked = locked, iconColor = iconColor)
                PlanFeatureColumn(iconId = icon2, label = "Members", value = title2, locked = locked, iconColor = iconColor)
            }
        }
    }
}


@Composable
fun PlanFeatureColumn(
    iconId: Int,
    label: String,
    value: String,
    locked: Boolean,
    iconColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = iconColor,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        if (locked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
