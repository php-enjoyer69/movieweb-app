<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-links">
        <a href="/" class="footer-link">Home</a>
        <a href="/movies" class="footer-link">Movies</a>
        <a href="/about" class="footer-link">About</a>
        <a href="/profile" class="footer-link">My Profile</a>
      </div>

      <div class="social-links">
        <a href="https://facebook.com" class="social-link" target="_blank" aria-label="Facebook">
          <Icon icon="mdi:facebook" />
        </a>
        <a href="https://twitter.com" class="social-link" target="_blank" aria-label="Twitter">
          <Icon icon="mdi:twitter" />
        </a>
        <a href="https://instagram.com" class="social-link" target="_blank" aria-label="Instagram">
          <Icon icon="mdi:instagram" />
        </a>
        <a href="https://tiktok.com" class="social-link" target="_blank" aria-label="TikTok">
          <Icon icon="ic:baseline-tiktok" />
        </a>
      </div>
    </div>

    <p class="footer-text">2024 MovieWeb. All Rights Reserved.</p>
  </footer>
</template>

<script setup>
import { Icon } from '@iconify/vue';
</script>

<style scoped>
body {
    font-family: 'Lexend', cursive;
    position: relative;
    min-height: 100vh;
    margin: 0;
    display: flex;
    flex-direction: column; 
    background-color: #f4f4f4;
    z-index: -1
}

.footer {
    background-color: #ececec;
    color: var(--el-color-primary);
    text-align: center;
    padding: 2rem 0;
    width: 100%;
    margin-top: auto;  
}

.footer-content {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.footer-links {
    display: flex;
    justify-content: center;
    margin-bottom: 1rem;
}

.footer-link {
    font-size: 1.2rem;
    margin: 0 20px;
    color: #333;
    text-decoration: none;
    transition: color 0.3s ease;
}

.footer-link:hover {
    color: #d5a1ea;
}

.social-links {
    display: flex;
    justify-content: center;
    margin-bottom: 1rem;
}

.social-link {
    font-size: 2rem;
    margin: 0 15px;
    color: #d5a1ea;
    transition: color 0.3s ease;
}

.social-link:hover {
    color: #fff;
}

.footer-text {
    font-size: 0.9rem;
    color: #888;
    margin-top: 1rem; 
}

</style>