import { Elysia } from "elysia";

export const userController = new Elysia()
  .get("/user/hello", () => "hello world");
