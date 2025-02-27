<template>
  <div class="page">
    <div class="banner">
      <img :src="require('@/assets/banner4.jpg')" alt="Banner with a Cat" class="banner-image" />
      <div class="banner-text">
        <h1 class="banner-title">This is the <span class="purple-text">right</span> place.</h1>
        <p class="banner-subtext">
          Your new favorite site to browse and review movies
        </p>
      </div>
    </div>

    <div class="content-container">
      <div class="watermark watermark-first"></div>

      <div class="carousel">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="(img, index) in firstCarousel" :key="index">
            <img :src="require(`@/assets/${img}`)" :alt="`Movie ${index + 1}`" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="text-content">
        <h2 class="box-title">Discover trending movies</h2>
        <p class="box-text">
          Explore the latest and most popular films that everyone is talking about. Stay updated with top-rated movies and discover new favorites.
        </p>
        <div class="button-container">
          <router-link to="/trending-now">
                <el-link :underline="false" class="browse-link">
                    See Top 10 Movies
                    <Icon icon="iconamoon:arrow-right-2-light" style="font-size: 20px;margin: 10px 0 8px" />
                </el-link>
            </router-link>
        </div>
      </div>
    </div>

    <div class="content-container">
      <div class="watermark watermark-second"></div>

      <div class="text-content reverse-layout">
        <h2 class="box-title">Rate and review</h2>
        <p class="box-text">
          Share your thoughts on movies you've watched. Leave ratings and reviews to help others decide what to watch next.
        </p>
        <div class="button-container">
          <router-link to="/movies">
                <el-link :underline="false" class="browse-link">
                    Browse all movies
                    <Icon icon="iconamoon:arrow-right-2-light" style="font-size: 20px;margin: 10px 0 8px" />
                </el-link>
            </router-link>
        </div>
      </div>

      <div class="carousel">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="(img, index) in secondCarousel" :key="index">
            <img :src="require(`@/assets/${img}`)" :alt="`Review ${index + 1}`" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

    <div class="content-container">
      <div class="watermark watermark-third"></div>

      <div class="carousel">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="(img, index) in thirdCarousel" :key="index">
            <img :src="require(`@/assets/${img}`)" :alt="`Star ${index + 7}`" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="text-content">
        <h2 class="box-title">Find out who's the top star</h2>
        <p class="box-text">
          Discover the most popular actors and actresses in the film industry. See which stars are trending right now and follow their latest works.
        </p>
        <div class="button-container">
          <router-link to="/top-celebrities">
                <el-link :underline="false" class="browse-link">
                  Check out trending celebrities
                    <Icon icon="iconamoon:arrow-right-2-light" style="font-size: 20px;margin: 10px 0 8px" />
                </el-link>
            </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Icon } from '@iconify/vue';

const firstCarousel = [
"1.jpg",
"2.jpg",
"3.jpg"
];

const secondCarousel = [
"4.jpg",
"5.jpg",
"6.jpg"
];

const thirdCarousel = [
"7.jpg",
"8.jpg",
"9.jpg"
];
</script>

<style scoped>
.carousel-container {
  border-radius: 10px;
}

.box-title, .box-text {
  border-radius: 8px;
}

.page {
    padding: 20px;
    margin: 0 auto;
    max-width: 1250px;
    background-color: #fff;
    border-radius: 8px;
}

.banner {
  position: relative;
  display: flex;
  justify-content: flex-start;
  width: 1250px;
  margin: -20px auto 0px -20px;
}

.banner-image {
  width: 100%;
  height: 100%;
  max-height: 400px;
  object-fit: cover;
}

.banner-text {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 50%;
  color: black;
  text-align: left;
  margin-left: 20px;
  margin-top: 40px;
}

.content-container {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 75px;
  margin-bottom: 50px;
}

.banner-title {
  margin: 0;
  color: #fff;
  font-size: 48px;
}

.banner-subtext {
  margin: 15px 0 0;
  font-size: 24px;
  font-family: "Quicksand", sans-serif;
  color: #fff;
}

.carousel {
  flex: 1;
}

.carousel-container {
  height: 250px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.text-content {
  flex: 2;
  height: 100%;
  padding: 20px;
  background-color: transparent;
  display: flex;
  flex-direction: column;
  gap: 1em;
}

.box-title {
  margin: 0;
  color: var(--el-color-primary);
  font-size: 40px;
}

.box-text {
  margin: 0;
  font-family: "Quicksand", sans-serif;
  font-size: 20px;
  margin-bottom: 20px;
}

.reverse-layout {
  text-align: right;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
}

.button-container {
  display: flex;
  justify-content: flex-start;
}

.watermark {
  position: absolute;
  background-image: url("https://api.iconify.design/mdi:movie.svg?color=%dcdcdc"); 
  background-size: contain;
  background-repeat: no-repeat;
  opacity: 0.1;
  width: 410px;
  height: 410px;
  z-index: 0;
}

.watermark-first {
  top: -20%;
  left: 60%;
  transform: rotate(-30deg);
}

.watermark-second {
  top: 54%;
  left: 25%;
  transform: translate(-50%, -50%) rotate(30deg);
}

.watermark-third {
  top: 55%;
  left: 70%;
  transform: translate(-50%, -50%) rotate(-20deg);
}

.purple-text {
  color: #9b4dca;
  font-style: italic; 
}

.browse-link {
    font-size: 1em;
    display: inline-flex;
    align-items: center;
    color: #333;
}
</style>
