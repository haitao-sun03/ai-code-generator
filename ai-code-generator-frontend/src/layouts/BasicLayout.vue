<script setup lang="ts">
import { ref } from 'vue';
import { RouterView } from 'vue-router';
import GlobalHeader from './components/GlobalHeader.vue';
import GlobalFooter from './components/GlobalFooter.vue';

// 菜单配置
const menuItems = ref([
  { key: 'home', label: '首页', path: '/' },
  { key: 'about', label: '关于', path: '/about' },
]);
</script>

<template>
  <a-layout class="layout-container">
    <!-- 顶部导航栏 -->
    <a-layout-header class="layout-header">
      <GlobalHeader :menu-items="menuItems" />
    </a-layout-header>
    
    <!-- 内容区域 -->
    <a-layout-content class="layout-content">
      <div class="content-container">
        <RouterView />
      </div>
    </a-layout-content>
    
    <!-- 底部页脚 -->
    <a-layout-footer class="layout-footer">
      <GlobalFooter />
    </a-layout-footer>
  </a-layout>
</template>

<style scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  position: fixed;
  z-index: 1;
  width: 100%;
  padding: 0;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.layout-content {
  padding: 0 50px;
  margin-top: 64px;
  flex: 1;
}

.content-container {
  padding: 24px;
  background: #fff;
  min-height: 280px;
  margin-top: 24px;
}

.layout-footer {
  text-align: center;
  background: #f0f2f5;
  padding: 24px 50px;
}

@media (max-width: 768px) {
  .layout-content {
    padding: 0 20px;
  }
  
  .content-container {
    padding: 16px;
  }
  
  .layout-footer {
    padding: 16px 20px;
  }
}
</style>