package com.cjack.ai.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.cjack.ai.data.local.dao.CprSessionDao;
import com.cjack.ai.data.local.dao.CprSessionDao_Impl;
import com.cjack.ai.data.local.dao.SosLogDao;
import com.cjack.ai.data.local.dao.SosLogDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CjackDatabase_Impl extends CjackDatabase {
  private volatile CprSessionDao _cprSessionDao;

  private volatile SosLogDao _sosLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `cpr_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startTime` INTEGER NOT NULL, `durationMs` INTEGER NOT NULL, `avgHeartRate` INTEGER NOT NULL, `avgSpo2` INTEGER NOT NULL, `outcome` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sos_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `heartRate` INTEGER NOT NULL, `spo2` INTEGER NOT NULL, `compressionForce` REAL NOT NULL, `compressionRate` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `aiSummary` TEXT NOT NULL, `notificationStatus` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '14d80221cb19c97f54e69feb826826c8')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `cpr_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `sos_logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCprSessions = new HashMap<String, TableInfo.Column>(6);
        _columnsCprSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCprSessions.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCprSessions.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCprSessions.put("avgHeartRate", new TableInfo.Column("avgHeartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCprSessions.put("avgSpo2", new TableInfo.Column("avgSpo2", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCprSessions.put("outcome", new TableInfo.Column("outcome", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCprSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCprSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCprSessions = new TableInfo("cpr_sessions", _columnsCprSessions, _foreignKeysCprSessions, _indicesCprSessions);
        final TableInfo _existingCprSessions = TableInfo.read(db, "cpr_sessions");
        if (!_infoCprSessions.equals(_existingCprSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "cpr_sessions(com.cjack.ai.data.local.entity.CprSessionEntity).\n"
                  + " Expected:\n" + _infoCprSessions + "\n"
                  + " Found:\n" + _existingCprSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsSosLogs = new HashMap<String, TableInfo.Column>(10);
        _columnsSosLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("heartRate", new TableInfo.Column("heartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("spo2", new TableInfo.Column("spo2", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("compressionForce", new TableInfo.Column("compressionForce", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("compressionRate", new TableInfo.Column("compressionRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("aiSummary", new TableInfo.Column("aiSummary", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSosLogs.put("notificationStatus", new TableInfo.Column("notificationStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSosLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSosLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSosLogs = new TableInfo("sos_logs", _columnsSosLogs, _foreignKeysSosLogs, _indicesSosLogs);
        final TableInfo _existingSosLogs = TableInfo.read(db, "sos_logs");
        if (!_infoSosLogs.equals(_existingSosLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "sos_logs(com.cjack.ai.data.local.entity.SosLogEntity).\n"
                  + " Expected:\n" + _infoSosLogs + "\n"
                  + " Found:\n" + _existingSosLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "14d80221cb19c97f54e69feb826826c8", "9715d459089754147eb135bf722b5ed3");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "cpr_sessions","sos_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `cpr_sessions`");
      _db.execSQL("DELETE FROM `sos_logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CprSessionDao.class, CprSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SosLogDao.class, SosLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CprSessionDao getCprSessionDao() {
    if (_cprSessionDao != null) {
      return _cprSessionDao;
    } else {
      synchronized(this) {
        if(_cprSessionDao == null) {
          _cprSessionDao = new CprSessionDao_Impl(this);
        }
        return _cprSessionDao;
      }
    }
  }

  @Override
  public SosLogDao getSosLogDao() {
    if (_sosLogDao != null) {
      return _sosLogDao;
    } else {
      synchronized(this) {
        if(_sosLogDao == null) {
          _sosLogDao = new SosLogDao_Impl(this);
        }
        return _sosLogDao;
      }
    }
  }
}
