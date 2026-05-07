<template>
    <div class="h-screen w-full flex bg-[#F5F5F7] overflow-hidden">
    <aside class="w-24 flex flex-col items-center py-8 bg-white border-r border-gray-100 z-20">
        <div class="w-12 h-12 bg-black rounded-2xl flex items-center justify-center mb-12 shadow-lg">
        <span class="text-white font-bold text-xl">K</span>
        </div>
        <nav class="flex-1 flex flex-col gap-6">
        <router-link to="/candidate/dashboard" class="p-4 rounded-2xl transition-all" active-class="bg-black text-white shadow-lg" inactive-class="text-gray-300 hover:text-black">
            <span class="text-xl">🏠</span>
        </router-link>
        <router-link to="/candidate/jobs" class="p-4 rounded-2xl transition-all" active-class="bg-black text-white shadow-lg" inactive-class="text-gray-300 hover:text-black">
            <span class="text-xl">💼</span>
        </router-link>
        <router-link to="/candidate/analysis" class="p-4 rounded-2xl transition-all" active-class="bg-black text-white shadow-lg" inactive-class="text-gray-300 hover:text-black">
            <span class="text-xl">📊</span>
        </router-link>
        </nav>
    </aside>

    <main class="flex-1 flex flex-col h-full overflow-hidden">
        <header class="h-16 w-full flex items-center justify-end px-8 bg-white/30 backdrop-blur-md">
        <div class="flex items-center gap-3">
            <span class="text-xs font-bold text-gray-400">DEC 2026 EXAM PREP</span> <div class="w-8 h-8 rounded-full bg-gradient-to-tr from-blue-500 to-purple-500 shadow-inner"></div>
        </div>
        </header>
        <div class="flex-1 overflow-y-auto no-scrollbar">
        <router-view />
        </div>
    </main>
    </div>
</template>