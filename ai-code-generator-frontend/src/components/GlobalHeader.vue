<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="240px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.svg" alt="智码工坊" />
            <h1 class="site-title">智码工坊</h1>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { useRouter } from 'vue-router'
import { type MenuProps, message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { logout } from '@/api/userController.ts'
import { LogoutOutlined, HomeOutlined } from '@ant-design/icons-vue'

const loginUserStore = useLoginUserStore()
const router = useRouter()
// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

// 菜单配置项
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/admin/appManage',
    label: '应用管理',
    title: '应用管理',
  },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    if (menuKey?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}

// 退出登录
const doLogout = async () => {
  const res = await logout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
.header {
  background: white;
  padding: 0 24px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid #e2e8f0;
  height: 64px;
  line-height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.25s ease-in-out;
  height: 64px;
  padding: 8px 0;
}

.header-left:hover {
  transform: scale(1.01);
}

.logo {
  height: 40px;
  width: auto;
  transition: all 0.25s ease-in-out;
  flex-shrink: 0;
}

.logo:hover {
  filter: drop-shadow(0 4px 8px rgba(59, 130, 246, 0.2));
}

.site-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1d4ed8;
  white-space: nowrap;
  flex-shrink: 0;
}

.ant-menu-horizontal {
  border-bottom: none !important;
  background: transparent;
  line-height: 62px;
}

:deep(.ant-menu-item) {
  height: 62px;
  line-height: 62px;
}

.user-login-status {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 现代化按钮样式覆盖 */
:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  border: none;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.25s ease-in-out;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

:deep(.ant-btn-primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

:deep(.ant-avatar) {
  border: 2px solid var(--primary-200);
  transition: all var(--transition-normal);
}

:deep(.ant-avatar:hover) {
  border-color: var(--primary-400);
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
}
</style>
