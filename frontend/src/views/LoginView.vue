<template>
  <div class="login-wrapper">
    <!-- Left Side: Random Image -->
    <div class="image-section" :style="{ backgroundImage: `url(${currentImage})` }">
      <div class="overlay">
        <h1>Sistema ERP</h1>
        <p>Gestión inteligente para tu negocio</p>
      </div>
    </div>

    <!-- Right Side: Login/Register Form -->
    <div class="form-section">
      <div class="form-container">
        <div class="brand">
          <img src="/Logo.jpeg" alt="Logo" class="logo-img" />
          <h2>{{ isRegistering ? 'Crear Cuenta' : 'Iniciar Sesión' }}</h2>
        </div>

        <form @submit.prevent="handleSubmit">
          <div v-if="isRegistering">
            <label>Nombre Completo</label>
            <input v-model="form.nombre" required placeholder="Juan Perez">

            <label>Cédula / RUC</label>
            <input v-model="form.cedula" required placeholder="1700...">

            <label>Dirección</label>
            <input v-model="form.direccion" placeholder="Av. Amazonas...">
            
            <label>Teléfono</label>
            <input v-model="form.telefono" placeholder="099...">
          </div>

          <label>Usuario (Email)</label>
          <input type="text" v-model="form.username" required placeholder="admin">

          <label>Contraseña</label>
          <input type="password" v-model="form.password" required placeholder="••••••">

          <button type="submit" class="btn btn-primary full-width">
            {{ isRegistering ? 'Registrarse' : 'Entrar' }}
          </button>
        </form>

        <p class="toggle-text">
          {{ isRegistering ? '¿Ya tienes cuenta?' : '¿No tienes cuenta?' }}
          <a href="#" @click.prevent="isRegistering = !isRegistering">
            {{ isRegistering ? 'Inicia Sesión' : 'Regístrate' }}
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const isRegistering = ref(false);
const currentImage = ref('');

const images = [
  '/images/atardecer.png',
  '/images/istockphoto-1453838542-612x612.jpg',
  '/images/montaña.png',
  '/images/xd.png'
];

const form = ref({
  username: '',
  password: '',
  nombre: '',
  cedula: '',
  direccion: '',
  telefono: '',
  role: 'CLIENT' // Default role
});

onMounted(() => {
  // Initialize with first image
  currentImage.value = images[0];
  
  // Carousel Interval
  setInterval(() => {
    const currentIndex = images.indexOf(currentImage.value);
    const nextIndex = (currentIndex + 1) % images.length;
    currentImage.value = images[nextIndex];
  }, 5000); // Change every 5 seconds
});

const handleSubmit = async () => {
  const endpoint = isRegistering.value ? '/api/auth/register' : '/api/auth/login';
  
  // Validation
  if (isRegistering.value) {
      const nameRegex = /^[A-Za-z0-9ñÑáéíóúÁÉÍÓÚ\s]+$/;
      if (!nameRegex.test(form.value.nombre)) {
          alert('El nombre solo puede contener letras, números y espacios.');
          return;
      }
  }

  try {
    const res = await fetch(`http://localhost:8080${endpoint}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    });

    if (res.ok) {
        const data = await res.json();
        if (isRegistering.value) {
            alert('Registro exitoso. Por favor inicia sesión.');
            isRegistering.value = false;
        } else {
            localStorage.setItem('user', JSON.stringify(data));
            router.push('/facturar');
        }
    } else {
        const errorText = await res.text();
        console.error("Error Response:", errorText);
        try {
             // Intenta parsear si es JSON
            const errorJson = JSON.parse(errorText);
            alert(`Error: ${errorJson.message || errorJson.error || 'Desconocido'}`);
        } catch (e) {
            alert(`Error del Servidor: ${errorText}`);
        }
    }
  } catch (e) {
    console.error(e);
    alert('Error de conexión con el servidor (Revisa si el Backend está corriendo).');
  }
};
</script>

<style scoped>
.login-wrapper {
  display: flex;
  height: 100vh;
  width: 100vw;
}

.image-section {
  width: 35%; /* Thinner image section */
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: flex-end;
  color: white;
  transition: background-image 1s ease-in-out;
}

/* ... existing overlay styles ... */
.image-section .overlay {
    background: linear-gradient(transparent, rgba(0,0,0,0.8));
    width: 100%;
    padding: 40px;
}

.form-section {
  flex: 1; /* Takes remaining space */
  /* Gradient: Top half White, Bottom half fading to Navy Blue */
  background: linear-gradient(180deg, #ffffff 55%, #001f3f 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

/* ... existing form container styles ... */
.form-container {
    width: 100%;
    max-width: 400px;
}

.brand {
    text-align: center;
    margin-bottom: 30px;
}

.logo-img {
    height: 150px; /* Larger, squared size as requested */
    width: auto;
    margin-bottom: 15px;
    mix-blend-mode: multiply; /* Optional: helps blend if logo has white bg */
}

.full-width { width: 100%; margin-top: 20px; padding: 12px; font-size: 1.1rem; }

.toggle-text {
    text-align: center;
    margin-top: 20px;
    color: #666;
}

.toggle-text a {
    color: var(--primary);
    font-weight: bold;
    text-decoration: none;
}
</style>