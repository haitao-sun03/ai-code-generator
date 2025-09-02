<template>
  <div id="userLoginPage">
    <h2 class="title">智码工坊 - 用户登录</h2>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码长度不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <div class="tips">
        没有账号
        <RouterLink to="/user/register">去注册</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { login } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const router = useRouter()
const loginUserStore = useLoginUserStore()

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  const res = await login(values)
  // 登录成功，把登录态保存到全局状态中
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
#userLoginPage {
  background: white;
  max-width: 480px;
  padding: var(--space-8);
  margin: var(--space-8) auto;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  border: 1px solid var(--neutral-200);
}

.title {
  text-align: center;
  margin-bottom: var(--space-8);
  font-size: var(--text-2xl);
  font-weight: var(--font-semibold);
  color: var(--primary-700);
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.tips {
  text-align: right;
  color: var(--neutral-500);
  font-size: var(--text-sm);
  margin-bottom: var(--space-4);
}

.tips a {
  color: var(--primary-600);
  text-decoration: none;
  font-weight: var(--font-medium);
  transition: color var(--transition-fast);
}

.tips a:hover {
  color: var(--primary-700);
}

/* 现代化表单样式 */
:deep(.ant-form-item) {
  margin-bottom: var(--space-5);
}

:deep(.ant-input),
:deep(.ant-input-password) {
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-md);
  border: 1px solid var(--neutral-300);
  transition: all var(--transition-fast);
  font-size: var(--text-base);
}

:deep(.ant-input:focus),
:deep(.ant-input-password:focus) {
  border-color: var(--primary-500);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

:deep(.ant-btn) {
  height: auto;
  padding: var(--space-3) var(--space-6);
  border-radius: var(--radius-md);
  font-weight: var(--font-medium);
  transition: all var(--transition-normal);
}

:deep(.ant-btn-primary) {
  background: var(--gradient-primary);
  border: none;
  box-shadow: var(--shadow-sm);
}

:deep(.ant-btn-primary:hover) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}
</style>
