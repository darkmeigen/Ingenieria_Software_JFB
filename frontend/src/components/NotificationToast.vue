<template>
  <div class="notification-container">
    <transition-group name="toast">
      <div v-for="toast in toasts" :key="toast.id" :class="['toast', toast.type]">
        <div class="icon">
            <span v-if="toast.type === 'success'">✅</span>
            <span v-else-if="toast.type === 'error'">❌</span>
            <span v-else>ℹ️</span>
        </div>
        <div class="message">{{ toast.message }}</div>
        <button class="close" @click="removeToast(toast.id)">×</button>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const toasts = ref([]);
let idCounter = 0;

const add = (message, type = 'info') => {
    const id = idCounter++;
    toasts.value.push({ id, message, type });
    setTimeout(() => removeToast(id), 3000);
};

const removeToast = (id) => {
    toasts.value = toasts.value.filter(t => t.id !== id);
};

defineExpose({ add });
</script>

<style scoped>
.notification-container {
    position: fixed; top: 20px; right: 20px; z-index: 9999;
    display: flex; flex-direction: column; gap: 10px;
}
.toast {
    min-width: 300px; padding: 15px; border-radius: 8px;
    background: white; box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    display: flex; align-items: center; gap: 10px;
    animation: slideIn 0.3s ease;
    border-left: 5px solid #ccc;
}
.toast.success { border-left-color: #4CAF50; }
.toast.error { border-left-color: #F44336; }

.message { flex: 1; font-size: 0.95rem; color: #333; }
.close { background: none; border: none; font-size: 1.2rem; cursor: pointer; color: #999; }

@keyframes slideIn {
    from { transform: translateX(100%); opacity: 0; }
    to { transform: translateX(0); opacity: 1; }
}
.toast-enter-active, .toast-leave-active { transition: all 0.3s ease; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateX(30px); }
</style>
