package com.cjack.ai.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cjack.ai.data.local.entity.SosLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SosLogDao_Impl implements SosLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SosLogEntity> __insertionAdapterOfSosLogEntity;

  public SosLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSosLogEntity = new EntityInsertionAdapter<SosLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sos_logs` (`id`,`timestamp`,`heartRate`,`spo2`,`compressionForce`,`compressionRate`,`latitude`,`longitude`,`aiSummary`,`notificationStatus`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SosLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindLong(3, entity.getHeartRate());
        statement.bindLong(4, entity.getSpo2());
        statement.bindDouble(5, entity.getCompressionForce());
        statement.bindLong(6, entity.getCompressionRate());
        statement.bindDouble(7, entity.getLatitude());
        statement.bindDouble(8, entity.getLongitude());
        if (entity.getAiSummary() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAiSummary());
        }
        if (entity.getNotificationStatus() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotificationStatus());
        }
      }
    };
  }

  @Override
  public Object insertSosLog(final SosLogEntity log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSosLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SosLogEntity>> getAllSosLogs() {
    final String _sql = "SELECT * FROM sos_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sos_logs"}, new Callable<List<SosLogEntity>>() {
      @Override
      @NonNull
      public List<SosLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfSpo2 = CursorUtil.getColumnIndexOrThrow(_cursor, "spo2");
          final int _cursorIndexOfCompressionForce = CursorUtil.getColumnIndexOrThrow(_cursor, "compressionForce");
          final int _cursorIndexOfCompressionRate = CursorUtil.getColumnIndexOrThrow(_cursor, "compressionRate");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAiSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "aiSummary");
          final int _cursorIndexOfNotificationStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationStatus");
          final List<SosLogEntity> _result = new ArrayList<SosLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SosLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpHeartRate;
            _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            final int _tmpSpo2;
            _tmpSpo2 = _cursor.getInt(_cursorIndexOfSpo2);
            final float _tmpCompressionForce;
            _tmpCompressionForce = _cursor.getFloat(_cursorIndexOfCompressionForce);
            final int _tmpCompressionRate;
            _tmpCompressionRate = _cursor.getInt(_cursorIndexOfCompressionRate);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAiSummary;
            if (_cursor.isNull(_cursorIndexOfAiSummary)) {
              _tmpAiSummary = null;
            } else {
              _tmpAiSummary = _cursor.getString(_cursorIndexOfAiSummary);
            }
            final String _tmpNotificationStatus;
            if (_cursor.isNull(_cursorIndexOfNotificationStatus)) {
              _tmpNotificationStatus = null;
            } else {
              _tmpNotificationStatus = _cursor.getString(_cursorIndexOfNotificationStatus);
            }
            _item = new SosLogEntity(_tmpId,_tmpTimestamp,_tmpHeartRate,_tmpSpo2,_tmpCompressionForce,_tmpCompressionRate,_tmpLatitude,_tmpLongitude,_tmpAiSummary,_tmpNotificationStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
