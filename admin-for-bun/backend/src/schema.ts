import { mysqlTable, varchar, int } from 'drizzle-orm/mysql-core';

export const users = mysqlTable('users', {
  id: int('id').primaryKey().autoincrement(),
  username: varchar('username', { length: 50 }),
  password: varchar('password', { length: 100 }),
  role: int('role'), // 1: 管理员
});
