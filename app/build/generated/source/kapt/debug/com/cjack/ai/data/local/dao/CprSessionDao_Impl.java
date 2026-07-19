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
import com.cjack.ai.data.local.entity.CprSessionEntity;
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
public final class CprSessionDao_Impl implements CprSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CprSessionEntity> __insertionAdapterOfCprSessionEntity;

  public CprSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCprSessionEntity = new EntityInsertionAdapter<CprSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `cpr_sessions` (`id`,`startTime`,`durationMs`,`avgHeartRate`,`avgSpo2`,`outcome`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CprSessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        statement.bindLong(3, entity.getDurationMs());
        statement.bindLong(4, entity.getAvgHeartRate());
        statement.bindLong(5, entity.getAvgSpo2());
        if (entity.getOutcome() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOutcome());
        }
      }
    };
  }

  @Override
  public Object insertSession(final CprSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCprSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CprSessionEntity>> getAllSessions() {
    final String _sql = "SELECT * FROM cpr_sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cpr_sessions"}, new Callable<List<CprSessionEntity>>() {
      @Override
      @NonNull
      public List<CprSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
          final int _cursorIndexOfAvgSpo2 = CursorUtil.getColumnIndexOrThrow(_cursor, "avgSpo2");
          final int _cursorIndexOfOutcome = CursorUtil.getColumnIndexOrThrow(_cursor, "outcome");
          final List<CprSessionEntity> _result = new ArrayList<CprSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CprSessionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpAvgHeartRate;
            _tmpAvgHeartRate = _cursor.getInt(_cursorIndexOfAvgHeartRate);
            final int _tmpAvgSpo2;
            _tmpAvgSpo2 = _cursor.getInt(_cursorIndexOfAvgSpo2);
            final String _tmpOutcome;
            if (_cursor.isNull(_cursorIndexOfOutcome)) {
              _tmpOutcome = null;
            } else {
              _tmpOutcome = _cursor.getString(_cursorIndexOfOutcome);
            }
            _item = new CprSessionEntity(_tmpId,_tmpStartTime,_tmpDurationMs,_tmpAvgHeartRate,_tmpAvgSpo2,_tmpOutcome);
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
