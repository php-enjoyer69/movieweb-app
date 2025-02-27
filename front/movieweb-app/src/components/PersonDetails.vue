<template>
  <div class="movie-detail-content">
    <img :src="getImageUrl(person.img)" alt="Person Image" class="movie-img" />
    <div class="movie-info">
      <h1>{{ person.name }} {{ person.surname }}</h1>
      <el-collapse-transition>
        <p v-if="showFullBio && person.biography">{{ person.biography }}</p>
        <p v-else>{{ truncatedBiography }}...</p>
      </el-collapse-transition>

      <el-link :underline="false" type="text" @click="toggleBiography">
        {{ showFullBio ? 'Show less' : 'Show more' }}
        <el-icon>
          <i :class="showFullBio ? ArrowUp : ArrowDown"></i>
        </el-icon>
      </el-link>
      <p>
        <strong class="highlight-text">Born</strong><span class="person-info">{{ formatDate(person.birthDate) }}, {{
          person.countryOfOrigin }}</span>
      </p>
      <div class="vote-section">
        <Icon icon="mdi:arrow-up" class="positive-score" style="font-size: 24px; margin-right: -10px;" />
        <el-link :underline="false" @click="vote('upvote')" :type="getVoteType('UPVOTE')"
          :style="getVoteStyle('UPVOTE')" class="upvote">
          Upvote
        </el-link>

        <Icon icon="mdi:arrow-down" class="negative-score" style="font-size: 24px; margin-right: -10px;" />
        <el-link :underline="false" @click="vote('downvote')" :type="getVoteType('DOWNVOTE')"
          :style="getVoteStyle('DOWNVOTE')" class="downvote">
          Downvote
        </el-link>
      </div>
      <div class="popularity-score">
        <p><strong>Popularity Score</strong></p>
        <p class="average-score"
          :class="{ 'positive-score': person.averageRating > 0, 'negative-score': person.averageRating < 0 }">
          {{ person.averageRating > 0 ? '+' : '' }}{{ person.averageRating }}
        </p>
      </div>
      <p>out of <strong>{{ person.voteCount }}</strong> votes</p>
    </div>
  </div>

  <PersonRoles :personId="person.id" />

  <PersonComments :personId="person.id" />

  <LoginRequiredModal v-if="loginModalVisible" :visible="loginModalVisible" @close="closeLoginModal" />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import PersonRoles from './PersonRoles.vue';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { Icon } from '@iconify/vue';
import LoginRequiredModal from '@/components/LoginRequiredModal.vue';
import PersonComments from './PersonComments.vue';

const props = defineProps({
  person: Object,
  user: Object,
});

const { user } = storeToRefs(useAuthStore());

const showFullBio = ref(false);
const maxBioLength = 200;
const truncatedBiography = ref('');
const roles = ref([]);
const loginModalVisible = ref(false);
const userVote = ref(null);

onMounted(async () => {
  await fetchUserVote();
  if (props.person && props.person.biography) {
    truncatedBiography.value = props.person.biography.length > maxBioLength
      ? props.person.biography.slice(0, maxBioLength)
      : props.person.biography;
  } else {
    truncatedBiography.value = '';
  }
});

const toggleBiography = () => {
  showFullBio.value = !showFullBio.value;
};

const formatDate = (timestamp) => {
  if (!timestamp) return '-';
  const date = new Date(timestamp);
  const padZero = (num) => (num < 10 ? `0${num}` : num);
  const months = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];
  const year = date.getFullYear();
  const month = months[date.getMonth()];
  const day = padZero(date.getDate());
  return `${day} ${month} ${year}`;
};

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const fetchUserVote = async () => {
  if (!user.value) return;
  try {
    const response = await axios.get(`/api/person/${props.person.id}/vote/${user.value.id}`);
    userVote.value = response.data?.voteType || null;
  } catch (error) {
    console.error('Error fetching user vote:', error);
  }
};

const vote = async (voteType) => {
  if (!user.value) {
    loginModalVisible.value = true;
    return;
  }

  const selectedVote = voteType === 'upvote' ? 'UPVOTE' : 'DOWNVOTE';

  try {
    const response = await axios.post(`/api/person/${props.person.id}/vote`, null, {
      params: {
        userId: user.value.id,
        voteType: selectedVote,
      },
    });

    await fetchUserVote();

    const updatedPerson = await axios.get(`/api/person/${props.person.id}`);
    props.person.averageRating = updatedPerson.data.averageRating;
    props.person.voteCount = updatedPerson.data.voteCount;

    ElMessage.success(`Score updated!`);
  } catch (error) {
    console.error("Error voting:", error);
    ElMessage.error('An error occurred while voting.');
  }
};

const getVoteType = (voteType) => {
  return userVote.value === voteType ? 'success' : 'default';
};

const getVoteStyle = (voteType) => {
  if (userVote.value === voteType) {
    return voteType === 'UPVOTE' ? { color: 'rgb(49, 155, 49)' } : { color: 'rgb(255, 67, 67)' };
  } else {
    return {};
  }
};

const closeLoginModal = () => {
  loginModalVisible.value = false;
};
</script>

<style scoped>
.movie-detail-content {
  display: flex;
  gap: 20px;
}

.movie-img {
  width: 300px;
  height: auto;
  border-radius: 8px;
  object-fit: contain;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  background-color: #333;
}

.movie-info {
  flex: 1;
}

h1 {
  font-size: 2em;
  margin-bottom: 50px;
  color: #333;
}

h3 {
  margin-bottom: 40px;
  color: #6d6d6d;
}

p {
  margin: 5px 0;
  line-height: 1.6;
  color: #333;
}

.el-link {
  margin-bottom: 20px;
  font-size: 16px;
  cursor: pointer;
}

.vote-section {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.popularity-score {
  margin-top: 5px;
  font-size: 18px;
  font-weight: bold;
}

.average-score {
  font-size: 32px;
  font-weight: bold;
}

.positive-score {
  color: rgb(49, 155, 49);
}

.negative-score {
  color: rgb(255, 67, 67);
}

.vote-section .el-link:hover {
  transition: color 0.3s ease;
}

.vote-section .el-link.upvote:hover {
  color: rgb(49, 155, 49);
}

.vote-section .el-link.downvote:hover {
  color: rgb(255, 67, 67);
}

.person-info {
  margin-left: 10px;
  color: #6d6d6d;
}
</style>
