package com.cjack.ai.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u00020\u0004X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/cjack/ai/data/local/CjackDatabase;", "Landroidx/room/RoomDatabase;", "()V", "cprSessionDao", "Lcom/cjack/ai/data/local/dao/CprSessionDao;", "getCprSessionDao", "()Lcom/cjack/ai/data/local/dao/CprSessionDao;", "sosLogDao", "Lcom/cjack/ai/data/local/dao/SosLogDao;", "getSosLogDao", "()Lcom/cjack/ai/data/local/dao/SosLogDao;", "app_debug"})
@androidx.room.Database(entities = {com.cjack.ai.data.local.entity.CprSessionEntity.class, com.cjack.ai.data.local.entity.SosLogEntity.class}, version = 1, exportSchema = false)
public abstract class CjackDatabase extends androidx.room.RoomDatabase {
    
    public CjackDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.cjack.ai.data.local.dao.CprSessionDao getCprSessionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.cjack.ai.data.local.dao.SosLogDao getSosLogDao();
}