import { ComputedRef, Ref } from 'vue'
export type LayoutKey = string
declare module "/Users/yangyida/Documents/project2026/GuangSha-Xinli/admin-for-bun/node_modules/nuxt/dist/pages/runtime/composables" {
  interface PageMeta {
    layout?: false | LayoutKey | Ref<LayoutKey> | ComputedRef<LayoutKey>
  }
}