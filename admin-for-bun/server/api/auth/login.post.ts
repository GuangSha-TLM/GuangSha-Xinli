import { defineEventHandler, readBody, proxyRequest } from 'h3'

export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  // 只转发 username 和 password 字段
  const { username, password } = body || {}
  const res = await proxyRequest(event, 'http://localhost:8080/auth/login', {
    method: 'POST',
    body: { username, password },
    headers: {
      'Content-Type': 'application/json',
    },
  })
  return res
})
