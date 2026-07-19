package com.cjack.ai.presentation.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u0007\b\t\n\u000b\fB\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0006\r\u000e\u000f\u0010\u0011\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen;", "", "route", "", "(Ljava/lang/String;)V", "getRoute", "()Ljava/lang/String;", "Ambulance", "Dashboard", "History", "Settings", "Splash", "Validation", "Lcom/cjack/ai/presentation/navigation/Screen$Ambulance;", "Lcom/cjack/ai/presentation/navigation/Screen$Dashboard;", "Lcom/cjack/ai/presentation/navigation/Screen$History;", "Lcom/cjack/ai/presentation/navigation/Screen$Settings;", "Lcom/cjack/ai/presentation/navigation/Screen$Splash;", "Lcom/cjack/ai/presentation/navigation/Screen$Validation;", "app_debug"})
public abstract class Screen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    
    private Screen(java.lang.String route) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen$Ambulance;", "Lcom/cjack/ai/presentation/navigation/Screen;", "()V", "app_debug"})
    public static final class Ambulance extends com.cjack.ai.presentation.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.cjack.ai.presentation.navigation.Screen.Ambulance INSTANCE = null;
        
        private Ambulance() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen$Dashboard;", "Lcom/cjack/ai/presentation/navigation/Screen;", "()V", "app_debug"})
    public static final class Dashboard extends com.cjack.ai.presentation.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.cjack.ai.presentation.navigation.Screen.Dashboard INSTANCE = null;
        
        private Dashboard() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen$History;", "Lcom/cjack/ai/presentation/navigation/Screen;", "()V", "app_debug"})
    public static final class History extends com.cjack.ai.presentation.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.cjack.ai.presentation.navigation.Screen.History INSTANCE = null;
        
        private History() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen$Settings;", "Lcom/cjack/ai/presentation/navigation/Screen;", "()V", "app_debug"})
    public static final class Settings extends com.cjack.ai.presentation.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.cjack.ai.presentation.navigation.Screen.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen$Splash;", "Lcom/cjack/ai/presentation/navigation/Screen;", "()V", "app_debug"})
    public static final class Splash extends com.cjack.ai.presentation.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.cjack.ai.presentation.navigation.Screen.Splash INSTANCE = null;
        
        private Splash() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/cjack/ai/presentation/navigation/Screen$Validation;", "Lcom/cjack/ai/presentation/navigation/Screen;", "()V", "app_debug"})
    public static final class Validation extends com.cjack.ai.presentation.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.cjack.ai.presentation.navigation.Screen.Validation INSTANCE = null;
        
        private Validation() {
        }
    }
}