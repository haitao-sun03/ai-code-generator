<template>
  <div id="userRegisterPage">
    <h2 class="title">智码工坊 - 用户注册</h2>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请确认密码' },
          { min: 8, message: '密码不能小于 8 位' },
          { validator: validateCheckPassword },
        ]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
      </a-form-item>
      <div class="tips">
        已有账号？
        <RouterLink to="/user/login">去登录</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { register } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { reactive } from 'vue'

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

/**
 * 验证确认密码
 * @param rule
 * @param value
 * @param callback
 */
const validateCheckPassword = (rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (value && value !== formState.userPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: API.UserRegisterRequest) => {
  const res = await register(values)
  // 注册成功，跳转到登录页面
  if (res.data.code === 0) {
    message.success('注册成功')
    router.push({
      path: '/user/login',
      replace: true,
    })
  } else {
    message.error('注册失败，' + res.data.message)
  }
}
</script>

<style scoped>
#userRegisterPage {
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
  margin-bottom: var(--space-4);
  color: var(--neutral-500);
  font-size: var(--text-sm);
  text-align: right;
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
