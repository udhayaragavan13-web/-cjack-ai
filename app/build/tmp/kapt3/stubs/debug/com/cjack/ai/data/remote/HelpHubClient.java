package com.cjack.ai.data.remote;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001:\u0001\u001fB\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006J\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u000fJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u001e\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\fH\u0082@\u00a2\u0006\u0002\u0010\u001dJ\u001e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/cjack/ai/data/remote/HelpHubClient;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "lastRecordedLocation", "Lkotlin/Pair;", "", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "getBaseUrl", "", "getDeviceLocation", "getEmergencyContacts", "", "getTrackingData", "Lcom/cjack/ai/data/remote/HelpHubClient$TrackingData;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendEmergencyStop", "", "sendTelemetry", "telemetry", "Lcom/cjack/ai/data/model/Telemetry;", "aiStatus", "(Lcom/cjack/ai/data/model/Telemetry;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerPhoneCallAndSms", "", "message", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerSos", "TrackingData", "app_debug"})
public final class HelpHubClient {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private kotlin.Pair<java.lang.Double, java.lang.Double> lastRecordedLocation;
    
    @javax.inject.Inject()
    public HelpHubClient(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBaseUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Pair<java.lang.Double, java.lang.Double> getDeviceLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getEmergencyContacts() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendTelemetry(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.model.Telemetry telemetry, @org.jetbrains.annotations.NotNull()
    java.lang.String aiStatus, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final java.lang.Object triggerPhoneCallAndSms(java.lang.String message, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object triggerSos(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.model.Telemetry telemetry, @org.jetbrains.annotations.NotNull()
    java.lang.String aiStatus, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTrackingData(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.cjack.ai.data.remote.HelpHubClient.TrackingData> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendEmergencyStop(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BO\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0018\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\t0\b\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u001b\u0010\u001c\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\t0\bH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\fH\u00c6\u0003Ja\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\u001a\b\u0002\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\t0\b2\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\fH\u00d6\u0001J\t\u0010$\u001a\u00020%H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR#\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006&"}, d2 = {"Lcom/cjack/ai/data/remote/HelpHubClient$TrackingData;", "", "patientLat", "", "patientLng", "ambulanceLat", "ambulanceLng", "route", "", "Lkotlin/Pair;", "distanceKm", "etaMins", "", "(DDDDLjava/util/List;DI)V", "getAmbulanceLat", "()D", "getAmbulanceLng", "getDistanceKm", "getEtaMins", "()I", "getPatientLat", "getPatientLng", "getRoute", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class TrackingData {
        private final double patientLat = 0.0;
        private final double patientLng = 0.0;
        private final double ambulanceLat = 0.0;
        private final double ambulanceLng = 0.0;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<kotlin.Pair<java.lang.Double, java.lang.Double>> route = null;
        private final double distanceKm = 0.0;
        private final int etaMins = 0;
        
        public TrackingData(double patientLat, double patientLng, double ambulanceLat, double ambulanceLng, @org.jetbrains.annotations.NotNull()
        java.util.List<kotlin.Pair<java.lang.Double, java.lang.Double>> route, double distanceKm, int etaMins) {
            super();
        }
        
        public final double getPatientLat() {
            return 0.0;
        }
        
        public final double getPatientLng() {
            return 0.0;
        }
        
        public final double getAmbulanceLat() {
            return 0.0;
        }
        
        public final double getAmbulanceLng() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<kotlin.Pair<java.lang.Double, java.lang.Double>> getRoute() {
            return null;
        }
        
        public final double getDistanceKm() {
            return 0.0;
        }
        
        public final int getEtaMins() {
            return 0;
        }
        
        public final double component1() {
            return 0.0;
        }
        
        public final double component2() {
            return 0.0;
        }
        
        public final double component3() {
            return 0.0;
        }
        
        public final double component4() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<kotlin.Pair<java.lang.Double, java.lang.Double>> component5() {
            return null;
        }
        
        public final double component6() {
            return 0.0;
        }
        
        public final int component7() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.cjack.ai.data.remote.HelpHubClient.TrackingData copy(double patientLat, double patientLng, double ambulanceLat, double ambulanceLng, @org.jetbrains.annotations.NotNull()
        java.util.List<kotlin.Pair<java.lang.Double, java.lang.Double>> route, double distanceKm, int etaMins) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}