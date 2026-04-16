package com.example.laundryledger.di

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.laundryledger.data.LedgerDao
import com.example.laundryledger.data.LedgerDatabase
import com.example.laundryledger.domain.LedgerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEncryptedDatabasePassphrase(@ApplicationContext context: Context): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
            
        val sharedPrefs = EncryptedSharedPreferences.create(
            context,
            "laundry_ledger_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        
        var dbHash = sharedPrefs.getString("db_hash", null)
        if (dbHash == null) {
            dbHash = UUID.randomUUID().toString()
            sharedPrefs.edit().putString("db_hash", dbHash).apply()
        }
        
        return dbHash.toByteArray()
    }

    @Provides
    @Singleton
    fun provideLedgerDatabase(
        @ApplicationContext context: Context,
        passphrase: ByteArray
    ): LedgerDatabase {
        val supportFactory = SupportFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            LedgerDatabase::class.java,
            LedgerDatabase.DATABASE_NAME
        )
        .openHelperFactory(supportFactory)
        .build()
    }

    @Provides
    @Singleton
    fun provideLedgerDao(database: LedgerDatabase): LedgerDao {
        return database.ledgerDao
    }

    @Provides
    @Singleton
    fun provideLedgerRepository(dao: LedgerDao): LedgerRepository {
        return LedgerRepository(dao)
    }
}
