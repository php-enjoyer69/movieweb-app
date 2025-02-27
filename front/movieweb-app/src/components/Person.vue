<template>
  <router-link :to="`/person/${person.id}`" class="person-container">
    <img :src="getImageUrl(person.img)" alt="Person Image" class="person-img" />
    <div class="person-name">{{ person.name }} {{ person.surname }}</div>

    <div v-if="person.roles && person.roles.length > 0" class="person-movies">
      <div class="movie-tags">
        <span 
          v-for="(role, index) in person.roles.slice(0, 3)" 
          :key="index" 
          class="movie-tag" 
          :title="role.movieTitle"
        >
          {{ role.movieTitle.length > 35 ? role.movieTitle.slice(0, 35) + '...' : role.movieTitle }}
        </span>
        <span v-if="person.roles.length > 3" class="more-movies">+{{ person.roles.length - 3 }}</span>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const props = defineProps({
  person: {
    type: Object,
    required: true,
  },
});

const getImageUrl = (imageName) => `/api/image/${imageName}`;
</script>

<style scoped>
.person-movies {
  padding: 5px 10px;
  font-size: 1.3em;
}

.movie-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 5px;
  margin-top: 5px;
}

.movie-tag {
  background-color: #f4f4f4;
  color: #858585;
  font-size: 0.65em;
  padding: 2px 5px;
  border-radius: 4px;
  white-space: nowrap;
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
}

.more-movies {
  background-color: #ddd;
  color: #666;
  font-size: 0.65em;
  padding: 2px 5px;
  border-radius: 4px;
}

.person-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 230px;
  height: 445px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  background-color: #f9f9f9;
  padding: 3px;
  box-sizing: border-box;
  text-decoration: none;
  color: inherit;
  transition: transform 0.3s, box-shadow 0.3s;
}

.person-img {
  width: 100%;
  height: 320px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 10px;
  background-color: transparent;
}

.person-container:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.person-name {
  margin-top: 13px;
  margin-bottom: -25px;
  font-size: 1.3em;
  font-weight: bold;
  padding: 0 10px;
  min-height: 48px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #333;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  display: block;
}
</style>
