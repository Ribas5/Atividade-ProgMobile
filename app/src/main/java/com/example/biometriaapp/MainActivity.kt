package com.example.biometriaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val biometricManager = BiometricManager.from(this)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                runOnUiThread {
                    Toast.makeText(applicationContext, "Autenticado com sucesso!", Toast.LENGTH_SHORT).show()
                    setContentView(R.layout.activity_main)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                runOnUiThread {
                    Toast.makeText(applicationContext, "Autenticação falhou", Toast.LENGTH_SHORT).show()
                }
            }
        })


        //Você pode usar outras autenticações mudando o BiometricManager.Authenticators.BIOMETRIC_STRONG
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Biométrica")
                .setSubtitle("Use sua digital para continuar")
                .setNegativeButtonText("Cancelar")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build()
            biometricPrompt.authenticate(promptInfo)
        } else {
            Toast.makeText(this, "Biometria não disponível", Toast.LENGTH_SHORT).show()
        }
    }
}
