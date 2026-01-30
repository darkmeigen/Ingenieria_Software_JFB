
<template>
  <div class="login-container">
    <div class="card login-card">
      <div class="login-header">
        <h2>Iniciar Sesión</h2>
        <p>Ingrese sus credenciales</p>
      </div>
      
      <form @submit.prevent="handleLogin">
        <label>Usuario</label>
        <input type="text" v-model="user" placeholder="admin" required>

        <label>Contraseña</label>
        <input type="password" v-model="pass" placeholder="••••••" required>

        <button type="submit" class="btn btn-primary full-width">Entrar</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const user = ref('');
const pass = ref('');
const router = useRouter();

const handleLogin = () => {
  if(user.value && pass.value) {
    // Simulamos login (Aquí conectaríamos con Spring Security luego)
    alert(`Bienvenido, ${user.value}`);
    router.push('/facturar');
  } else {
    alert("Por favor complete los campos");
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}
.login-card { width: 100%; max-width: 400px; }
.login-header { text-align: center; margin-bottom: 30px; }
.full-width { width: 100%; margin-top: 10px; }
</style>