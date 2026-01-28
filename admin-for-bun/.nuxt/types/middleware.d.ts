import type { NavigationGuard } from 'vue-router'
export type MiddlewareKey = string
declare module "/Users/yangyida/Documents/project2026/GuangSha-Xinli/admin-for-bun/node_modules/nuxt/dist/pages/runtime/composables" {
  interface PageMeta {
    middleware?: MiddlewareKey | NavigationGuard | Array<MiddlewareKey | NavigationGuard>
  }
}