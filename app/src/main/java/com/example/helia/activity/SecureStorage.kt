package com.example.helia.activity

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecureStorage {

    private const val PREF_NAME = "HeliaSecureLogin"
    private const val KEY_PASSWORD = "password"
    private const val KEY_IV = "password_iv"

    private const val KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "HeliaPasswordKey"

    private fun getKey(): SecretKey {

        val keyStore = java.security.KeyStore.getInstance(KEYSTORE)
        keyStore.load(null)

        val existingKey = keyStore.getKey(KEY_ALIAS, null)

        if (existingKey is SecretKey) {
            return existingKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(spec)

        return keyGenerator.generateKey()
    }

    fun savePassword(context: Context, password: String) {

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getKey()
        )

        val encryptedPassword = cipher.doFinal(
            password.toByteArray(StandardCharsets.UTF_8)
        )

        val iv = cipher.iv

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                KEY_PASSWORD,
                Base64.encodeToString(
                    encryptedPassword,
                    Base64.DEFAULT
                )
            )
            .putString(
                KEY_IV,
                Base64.encodeToString(
                    iv,
                    Base64.DEFAULT
                )
            )
            .apply()
    }

    fun getPassword(context: Context): String? {

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        val encryptedPassword = prefs.getString(
            KEY_PASSWORD,
            null
        ) ?: return null

        val ivString = prefs.getString(
            KEY_IV,
            null
        ) ?: return null

        return try {

            val encryptedData = Base64.decode(
                encryptedPassword,
                Base64.DEFAULT
            )

            val iv = Base64.decode(
                ivString,
                Base64.DEFAULT
            )

            val cipher = Cipher.getInstance(
                "AES/GCM/NoPadding"
            )

            val spec = GCMParameterSpec(
                128,
                iv
            )

            cipher.init(
                Cipher.DECRYPT_MODE,
                getKey(),
                spec
            )

            val decryptedPassword = cipher.doFinal(
                encryptedData
            )

            String(
                decryptedPassword,
                StandardCharsets.UTF_8
            )

        } catch (e: Exception) {
            null
        }
    }

    fun clearPassword(context: Context) {

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .clear()
            .apply()
    }
}