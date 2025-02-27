<template>
  <div class="movie-crew">
    <div v-if="director && director.person">
      <p>
        Director
        <router-link :to="`/person/${director.person.id}`" class="role-link">
          {{ director.person.name }} {{ director.person.surname }}
        </router-link>
      </p>
    </div>

    <div v-if="producer && producer.length > 0">
      <p>
        Producer
        <span v-for="(person, index) in producer.slice(0, 3)" :key="index">
          <router-link :to="`/person/${person.id}`" class="role-link">
            {{ person.name }} {{ person.surname }}
          </router-link>
        </span>
      </p>
    </div>

    <div v-if="screenwriter && screenwriter.length > 0">
      <p>
        Screenwriter
        <span v-for="(person, index) in screenwriter.slice(0, 3)" :key="index">
          <router-link :to="`/person/${person.id}`" class="role-link">
            {{ person.name }} {{ person.surname }}
          </router-link>
        </span> 
      </p>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  props: {
    movieId: {
      type: Number,
      required: true
    }
  },
  setup(props) {
    const director = ref(null);
    const producer = ref([]);
    const screenwriter = ref([]);

    const fetchRoles = async () => {
      try {
        const response = await axios.get(`/api/movie/${props.movieId}/roles`);
        const roles = response.data;

        director.value = roles.find(role => role.role === 'director');
        producer.value = roles.filter(role => role.role === 'producer').map(role => role.person);
        screenwriter.value = roles.filter(role => role.role === 'screenwriter').map(role => role.person);
      } catch (error) {
        console.error('Error fetching roles:', error);
      }
    };

    onMounted(() => {
      fetchRoles();
    });

    return {
      director,
      producer,
      screenwriter,
    };
  }
};
</script>

<style scoped>
.role-link {
  font-size: 0.9rem;
  margin-top: -5px;
  padding: 0 8px;
  color: #555;
  cursor: pointer;
  text-decoration: none;
}

.role-link:hover {
  color: #6a2a8c;
  text-decoration: none;
}

p {
  font-size: 0.9rem;
  color: #888;
  margin: 0;
  line-height: 1.3;
}

.movie-crew {
  margin-bottom: 20px;
}
</style>
