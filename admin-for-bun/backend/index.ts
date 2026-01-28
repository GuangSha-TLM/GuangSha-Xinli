const server = Bun.serve({
  port: 3000,
  fetch(req) {
    return new Response(
      JSON.stringify({
        message: "Hello from Bun backend 🚀"
      }),
      { headers: { "Content-Type": "application/json" } }
    )
  }
})

console.log(`Server running on http://localhost:${server.port}`)