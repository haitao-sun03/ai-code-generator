<template>
  <div class="global-header">
    <div class="logo-container">
      <img src="@/assets/logo.svg" alt="Logo" class="logo" />
      <h1 class="site-title">AI代码生成平台</h1>
    </div>

    <div class="menu-container">
      <a-menu v-model:selectedKeys="current" mode="horizontal" @click="handleMenuClick">
        <a-menu-item v-for="item in menuItems" :key="item.key">
          {{ item.label }}
        </a-menu-item>
      </a-menu>
    </div>

    <div class="user-container">

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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { isLogin, logout } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'
import { LogoutOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const loginUserStore = useLoginUserStore()

// 定义props
const props = defineProps<{
  menuItems: Array<{
    key: string
    label: string
    path: string
  }>
}>()

// 当前选中的菜单项
const current = ref<string[]>([])

// 处理菜单点击
const handleMenuClick = ({ key }) => {
  // const menuItem = props.menuItems.find((item) => item.key === info.key)
  // if (menuItem) {
  router.push({
    path: key,
  })
  // }
}

router.afterEach((to, from, next) => {
  current.value = [to.path]
})

const  doLogout = async ()=> {
  const res = await logout();
  if(res.data.code === 0 && res.data.data) {
    router.push({
      path: '/user/login',
    })
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success("已退出登录")
  }else {
    message.error(res.data.message)
  }
}
</script>

<style scoped>
.global-header {
  display: flex;
  align-items: center;
  height: 64px;
  padding: 0 24px;
  width: 100%;
}

.logo-container {
  display: flex;
  align-items: center;
  margin-right: 30px;
}

.logo {
  height: 32px;
  margin-right: 12px;
}

.site-title {
  margin: 0;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 600;
  font-size: 18px;
  white-space: nowrap;
}

.menu-container {
  flex: 1;
}

.user-container {
  margin-left: 20px;
}

@media (max-width: 768px) {
  .global-header {
    padding: 0 12px;
  }

  .site-title {
    display: none;
  }

  .logo-container {
    margin-right: 15px;
  }
}
</style>
