<template>
  <a-layout class="basic-layout">
    <!-- 顶部导航栏 -->
    <GlobalHeader />
    <!-- 主要内容区域 -->
    <a-layout-content class="main-content">
      <router-view />
    </a-layout-content>
    <!-- 底部版权信息 -->
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  background: var(--neutral-50);
  min-height: 100vh;
}

.main-content {
  width: 100%;
  padding: 0;
  background: var(--neutral-50);
  margin: 0;
  min-height: calc(100vh - 64px - 60px); /* 减去header和footer高度 */
  transition: all var(--transition-normal);
}

/* 全局滚动条样式 */
:deep(*::-webkit-scrollbar) {
  width: 8px;
  height: 8px;
}

:deep(*::-webkit-scrollbar-track) {
  background: var(--neutral-100);
  border-radius: var(--radius-sm);
}

:deep(*::-webkit-scrollbar-thumb) {
  background: var(--neutral-300);
  border-radius: var(--radius-sm);
  transition: background var(--transition-fast);
}

:deep(*::-webkit-scrollbar-thumb:hover) {
  background: var(--primary-400);
}

/* 全局选择文本样式 */
:deep(::selection) {
  background: rgba(59, 130, 246, 0.2);
  color: var(--primary-800);
}
</style>
