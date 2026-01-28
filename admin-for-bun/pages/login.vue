<template>
  <div class="login-container">
    <form @submit.prevent="handleLogin">
      <input v-model="username" placeholder="用户名" required />
      <input v-model="password" type="password" placeholder="密码" required />
      <button type="submit">登录</button>
    </form>
    <div v-if="response" class="response">
      <pre>{{ response }}</pre>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const username = ref('')
const password = ref('')
const response = ref('')

async function handleLogin() {
  try {
    const res = await $fetch('/api/auth/login', {
      method: 'POST',
      body: { username: username.value, password: password.value },
    })
    response.value = JSON.stringify(res, null, 2)
    if (res && res.code === 0) {
      // 登录成功，跳转到管理面板主页
      window.location.href = '/dashboard'
    }
  } catch (err: any) {
    response.value = err?.data ? JSON.stringify(err.data, null, 2) : err.message
  }
}
</script>

<style scoped>
.login-container {
  max-width: 320px;
  margin: 3rem auto;
  padding: 2rem;
  border: 1px solid #eee;
  border-radius: 8px;
  background: #fafbfc;
}
input {
  display: block;
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.5rem;
  font-size: 1rem;
}
button {
  width: 100%;
  padding: 0.5rem;
  background: #42b983;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}
.response {
  margin-top: 1rem;
  background: #f6f8fa;
  padding: 1rem;
  border-radius: 4px;
  font-size: 0.95rem;
}
</style>
