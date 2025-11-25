package com.smk1001.dragonscircuit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smk1001.dragonscircuit.ui.theme.DragonsCircuitTheme

private enum class GameScreen(val title: String) {
    Dashboard("Dashboard"),
    Training("Training"),
    Recovery("Recovery"),
    Boss("Boss"),
    Settings("Settings")
}

data class PlayerStats(
    val name: String,
    val level: Int,
    val xp: Int,
    val hp: Int,
    val ap: Int,
    val attack: Int,
    val defence: Int,
    val speed: Int,
    val technique: Int,
    val criticalChance: Int,
    val criticalDamage: Int,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DragonsCircuitTheme {
                val player = PlayerStats(
                    name = "Jade Striker",
                    level = 12,
                    xp = 1840,
                    hp = 340,
                    ap = 5,
                    attack = 72,
                    defence = 58,
                    speed = 64,
                    technique = 70,
                    criticalChance = 18,
                    criticalDamage = 145,
                )

                Surface(modifier = Modifier.fillMaxSize()) {
                    GameShell(player)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GameShell(player: PlayerStats) {
    var currentScreen by remember { mutableStateOf(GameScreen.Dashboard) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = currentScreen.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                actions = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GameScreen.values().forEach { screen ->
                            TextButton(onClick = { currentScreen = screen }) {
                                Text(
                                    text = screen.title,
                                    color = if (screen == currentScreen) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)

        when (currentScreen) {
            GameScreen.Dashboard -> DashboardScreen(player, modifier)
            GameScreen.Training -> TrainingScreen(player, modifier)
            GameScreen.Recovery -> RecoveryScreen(player, modifier)
            GameScreen.Boss -> BossScreen(player, modifier)
            GameScreen.Settings -> SettingsScreen(player, modifier)
        }
    }
}

@Composable
private fun DashboardScreen(player: PlayerStats, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome back, ${player.name}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        StatSummaryCard(player)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard(
                title = "Training",
                description = "Sharpen your combos",
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "Recovery",
                description = "Patch up after the arena",
                modifier = Modifier.weight(1f)
            )
        }
        AttributeGrid(player)
    }
}

@Composable
private fun TrainingScreen(player: PlayerStats, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Training Grounds",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Focus on attributes that boost your fighting style.",
            style = MaterialTheme.typography.bodyMedium
        )
        TrainingTrack(
            title = "Speed Drills",
            progress = 0.65f,
            highlight = "Speed boosts dash recovery and combo setup",
            primaryAttribute = "Speed",
            attributeValue = player.speed
        )
        TrainingTrack(
            title = "Precision Forms",
            progress = 0.4f,
            highlight = "Technique improves cancel windows",
            primaryAttribute = "Technique",
            attributeValue = player.technique
        )
        TrainingTrack(
            title = "Power Conditioning",
            progress = 0.3f,
            highlight = "Attack raises base damage",
            primaryAttribute = "Attack",
            attributeValue = player.attack
        )
    }
}

@Composable
private fun RecoveryScreen(player: PlayerStats, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Recovery Bay",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        StatBox("HP", "${player.hp} / 400", 0.85f)
        StatBox("AP", "${player.ap} available", player.ap / 10f)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Rest Options", style = MaterialTheme.typography.titleMedium)
                RecoveryOption("Meditate", "Restore AP and reduce cooldown timers")
                RecoveryOption("Field Treatment", "Patch HP using stored kits")
                RecoveryOption("Guild Support", "Call allies for a defensive buff")
            }
        }
    }
}

@Composable
private fun BossScreen(player: PlayerStats, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Boss Encounter",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Crimson Wyrm", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = "Fire / Aerial Control")
                Text(text = "Recommended Stats: Attack 70+, Defence 60+, Speed 60+")
                Divider()
                Text(text = "Your Loadout", fontWeight = FontWeight.SemiBold)
                AttributeRow(label = "Attack", value = player.attack)
                AttributeRow(label = "Defence", value = player.defence)
                AttributeRow(label = "Speed", value = player.speed)
                AttributeRow(label = "Technique", value = player.technique)
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Boss Notes", style = MaterialTheme.typography.titleMedium)
                Text(text = "Break aerial control with well-timed anti-air and punish windows after the wing slam.")
                Text(text = "Critical Chance and Damage are applied after stagger — aim for combo extenders.")
            }
        }
    }
}

@Composable
private fun SettingsScreen(player: PlayerStats, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Profile", style = MaterialTheme.typography.titleMedium)
                AttributeRow(label = "Name", value = player.name)
                AttributeRow(label = "Level", value = "${player.level}")
                AttributeRow(label = "XP", value = "${player.xp}")
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Battle Preferences", style = MaterialTheme.typography.titleMedium)
                Text(text = "Auto-Guard: On")
                Text(text = "Hit Effects: High")
                Text(text = "Vibration: Enabled")
            }
            LinearProgressIndicator(progress = { progress })
        }
    }
}

@Composable
private fun StatSummaryCard(player: PlayerStats) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Level ${player.level}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "XP", fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { player.xp / 2500f })
            StatRow(label = "HP", value = "${player.hp}")
            StatRow(label = "AP", value = "${player.ap}")
        }
    }
}

@Composable
private fun QuickActionCard(title: String, description: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun AttributeGrid(player: PlayerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Attributes", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AttributeRow(label = "Attack", value = player.attack)
                    AttributeRow(label = "Defence", value = player.defence)
                    AttributeRow(label = "Speed", value = player.speed)
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AttributeRow(label = "Technique", value = player.technique)
                    AttributeRow(label = "Critical Chance", value = "${player.criticalChance}%")
                    AttributeRow(label = "Critical Damage", value = "${player.criticalDamage}%")
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}

@Composable
private fun AttributeRow(label: String, value: Int) {
    AttributeRow(label = label, value = "$value")
}

@Composable
private fun AttributeRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TrainingTrack(
    title: String,
    progress: Float,
    highlight: String,
    primaryAttribute: String,
    attributeValue: Int
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Text(text = highlight, style = MaterialTheme.typography.bodyMedium)
                }
                Box(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ).padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "$primaryAttribute $attributeValue", fontWeight = FontWeight.Bold)
                }
            }
            LinearProgressIndicator(progress = { progress })
        }
    }
}

@Composable
private fun StatBox(title: String, value: String, progress: Float) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            LinearProgressIndicator(progress = { progress })
        }
    }
}

@Composable
private fun RecoveryOption(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, fontWeight = FontWeight.SemiBold)
        Text(text = description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameShell() {
    DragonsCircuitTheme {
        GameShell(
            PlayerStats(
                name = "Jade Striker",
                level = 12,
                xp = 1840,
                hp = 340,
                ap = 5,
                attack = 72,
                defence = 58,
                speed = 64,
                technique = 70,
                criticalChance = 18,
                criticalDamage = 145,
            )
        )
    }
}
