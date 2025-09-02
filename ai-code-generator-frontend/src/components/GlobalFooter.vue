<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="copyright">
        <a
          href="https://github.com/haitao-sun03/ai-code-generator"
          target="_blank"
          rel="noopener noreferrer"
          class="author-link"
        >
          智码工坊
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// 无需额外的响应式数据
</script>

<style scoped>
.footer {
  background: white;
  backdrop-filter: blur(20px);
  text-align: center;
  padding: var(--space-6);
  margin-top: 0;
  border-top: 1px solid var(--neutral-200);
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.04);
  position: relative;
}

.footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 2px;
  background: var(--gradient-primary);
  border-radius: var(--radius-full);
}

.footer-content {
  position: relative;
  z-index: 1;
}

.copyright {
  margin: 0;
  color: var(--neutral-600);
  font-size: var(--text-sm);
  transition: color var(--transition-fast);
}

.author-link {
  color: var(--primary-600);
  text-decoration: none;
  font-weight: var(--font-medium);
  transition: all var(--transition-fast);
}

.author-link:hover {
  color: var(--primary-700);
  text-decoration: underline;
}
</style>
