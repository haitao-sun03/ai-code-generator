<template>
  <div class="app-card" :class="{ 'app-card--featured': featured }">
    <div class="app-preview">
      <img v-if="app.cover" :src="app.cover" :alt="app.appName" />
      <div v-else class="app-placeholder">
        <img src="@/assets/defaultAppCover.svg" alt="默认应用封面" />
      </div>
      <div class="app-overlay">
        <a-space>
          <a-button type="primary" @click="handleViewChat">查看对话</a-button>
          <a-button v-if="app.deployKey" type="default" @click="handleViewWork">查看作品</a-button>
        </a-space>
      </div>
    </div>
    <div class="app-info">
      <div class="app-info-left">
        <a-avatar :src="app.user?.userAvatar" :size="40">
          {{ app.user?.userName?.charAt(0) || 'U' }}
        </a-avatar>
      </div>
      <div class="app-info-right">
        <h3 class="app-title">{{ app.appName || '未命名应用' }}</h3>
        <p class="app-author">
          {{ app.user?.userName || (featured ? '官方' : '未知用户') }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  app: API.AppVO
  featured?: boolean
}

interface Emits {
  (e: 'view-chat', appId: string | number | undefined): void
  (e: 'view-work', app: API.AppVO): void
}

const props = withDefaults(defineProps<Props>(), {
  featured: false,
})

const emit = defineEmits<Emits>()

const handleViewChat = () => {
  emit('view-chat', props.app.id)
}

const handleViewWork = () => {
  emit('view-work', props.app)
}
</script>

<style scoped>
.app-card {
  background: white;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--neutral-200);
  transition: all var(--transition-normal);
  cursor: pointer;
  position: relative;
}

.app-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--gradient-primary);
  opacity: 0;
  transition: opacity var(--transition-normal);
  z-index: 0;
}

.app-card:hover::before {
  opacity: 0.02;
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary-200);
}

.app-card--featured {
  border: 2px solid var(--primary-200);
  background: linear-gradient(135deg, white 0%, var(--primary-50) 100%);
}

.app-card--featured::after {
  content: '精选';
  position: absolute;
  top: var(--space-3);
  right: var(--space-3);
  background: var(--gradient-primary);
  color: white;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  z-index: 2;
}

.app-preview {
  height: 200px;
  background: var(--neutral-100);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.app-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.app-card:hover .app-preview img {
  transform: scale(1.05);
}

.app-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--neutral-50);
}

.app-placeholder img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.app-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(30, 58, 138, 0.8);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all var(--transition-normal);
  z-index: 2;
}

.app-card:hover .app-overlay {
  opacity: 1;
}

.app-overlay .ant-space {
  gap: var(--space-3) !important;
}

.app-info {
  padding: var(--space-4);
  display: flex;
  align-items: center;
  gap: var(--space-3);
  position: relative;
  z-index: 1;
}

.app-info-left {
  flex-shrink: 0;
}

.app-info-right {
  flex: 1;
  min-width: 0;
}

.app-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin: 0 0 var(--space-1);
  color: var(--neutral-800);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color var(--transition-fast);
}

.app-card:hover .app-title {
  color: var(--primary-700);
}

.app-author {
  font-size: var(--text-sm);
  color: var(--neutral-600);
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 现代化按钮样式覆盖 */
:deep(.ant-btn) {
  border-radius: var(--radius-md);
  font-weight: var(--font-medium);
  transition: all var(--transition-normal);
  border: none;
}

:deep(.ant-btn-primary) {
  background: var(--gradient-primary);
  box-shadow: var(--shadow-sm);
}

:deep(.ant-btn-primary:hover) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

:deep(.ant-btn-default) {
  background: white;
  color: var(--primary-600);
  border: 1px solid var(--primary-200);
}

:deep(.ant-btn-default:hover) {
  background: var(--primary-50);
  border-color: var(--primary-300);
  color: var(--primary-700);
}

:deep(.ant-avatar) {
  border: 2px solid var(--primary-100);
  transition: all var(--transition-normal);
}

.app-card:hover :deep(.ant-avatar) {
  border-color: var(--primary-300);
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
}
</style>
