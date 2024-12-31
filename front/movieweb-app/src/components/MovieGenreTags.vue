<template>
  <div class="flex gap-2" v-if="genres && genres.length">
    <el-tag
      v-for="genre in genres"
      :key="genre.id"
      :class="getTagClass(genre.id)" 
      effect="dark"
      round
    >
      {{ genre.name }}
    </el-tag>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  genres: {
    type: Array,
    default: () => [],
  },
});

const getTagClass = (genreId) => {
  const genreClasses = {
    1: 'tag-purple',
    2: 'tag-dark-purple',
    3: 'tag-light-purple',
    4: 'tag-indigo',
    5: 'tag-pink',
    6: 'tag-default',
    7: 'tag-dark-gray'
  };

  return genreClasses[genreId] || 'tag-default';
};
</script>

<style scoped>
.flex {
  display: flex;
  gap: 0.2rem;
}

.tag-purple {
  background-color: #3d0066;
  color: white;
}

.tag-dark-purple {
  background-color: #6a2a8c;
  color: white;
}

.tag-light-purple {
  background-color: #d5a1ea;
  border-color: #ba84d0;
  color: white;
}

.tag-indigo {
  background-color: #9b4dca;
  border-color: #8241ab;
  color: white;
}

.tag-pink {
  background-color: #ff81d7;
  border-color: #eb79c7;
  color: white;
}

.tag-default {
  background-color: #f0f0f0;
  border-color: #d9d9d9;
  color: #343a40;
}

.tag-dark-gray {
  background-color: #303135;
  border-color: #171717;
  color: white;
}
</style>
