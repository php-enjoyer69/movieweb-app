<template>
  <div>
    <el-button type="success" class="rate-movie-button" v-if="user" :disabled="hasReviewed" @click="toggleRatingForm"
      :title="hasReviewed ? 'You have already rated this movie' : ''">
      <Icon icon="mdi:star-outline" class="star-icon" />
      {{ showRatingForm ? 'Hide Rating Form' : 'Rate this movie' }}
    </el-button>

    <el-button type="success" class="rate-movie-button" v-if="!user" @click="showLoginModal">
      <Icon icon="mdi:star-outline" class="star-icon" />
      Rate this movie
    </el-button>

    <el-button type="success" class="rate-movie-button" v-if="user" @click="toggleWatchlist"
      :title="isInWatchlist ? 'Remove from Watchlist' : ''">
      <Icon :icon="isInWatchlist ? 'mdi:check' : 'mdi:plus'" class="plus-icon" />
      {{ isInWatchlist ? 'In Watchlist' : 'Add to Watchlist' }}
    </el-button>

    <el-button type="success" class="rate-movie-button" v-if="!user" @click="showLoginModal">
      <Icon icon="mdi:plus" class="plus-icon" />
      Add to watchlist
    </el-button>

    <span class="heart-icon" v-if="user" @click="toggleFavorite"
      :title="isFavorite ? 'Remove from favorites' : 'Add to favorites'">
      <Icon :icon="isFavorite ? 'mdi:heart' : 'mdi:heart-outline'" />
    </span>

    <span class="heart-icon" v-if="!user" @click="showLoginModal" :title="'Log in to add to favorites'">
      <Icon icon="mdi:heart-outline" />
    </span>

    <el-link type="primary" class="link-button" :underline="false" v-if="hasReviewed" @click="toggleEditReviewForm">
      <Icon icon="mdi:pencil" class="star-icon" style="margin-right: 5px;" />
      Edit your review
    </el-link>

    <el-link type="danger" class="delete-link-button" :underline="false" v-if="hasReviewed" @click="deleteReview">
  <Icon icon="mdi:trash-can-outline" class="trash-icon" />
  Delete review
</el-link>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Icon } from '@iconify/vue';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';

const { user } = storeToRefs(useAuthStore());

const props = defineProps({
  hasReviewed: Boolean,
  toggleRatingForm: Function,
  addToWatchlist: Function,
  showLoginModal: Function,
  toggleFavorite: Function,
  isFavorite: Boolean,
  toggleWatchlist: Function,
  isInWatchlist: Boolean,
  showRatingForm: Boolean,
  toggleEditReviewForm: Function,
  deleteReview: Function
});
</script>

<style scoped>
.rate-movie-button {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  margin-top: 5px;
  font-size: 1em;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  line-height: 1.5;
}

.link-button {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  margin-top: 5px;
  font-size: 1em;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  line-height: 1.5;
}

.delete-link-button {
  display: inline-flex;
  align-items: center;
  padding: 5px 5px;
  margin-top: 5px;
  font-size: 1em;
  border: none;
  border-radius: 1px;
  cursor: pointer;
  transition: background-color 0.3s;
  line-height: 1.5;
  color: rgb(255, 66, 66);
}

.delete-link-button:hover {
  color: #ff716c;
}

.trash-icon {
  font-size: 1.2em;
  margin-right: 5px;
}

.rate-movie-button .star-icon,
.rate-movie-button .plus-icon {
  font-size: 1.2em;
  margin-left: -4px;
  margin-right: 5px;
  color: #9b4dca;
}

.rate-movie-button:disabled {
  cursor: pointer !important;
  background-color: #5a5a5a;
}

.rate-movie-button:disabled:hover {
  background-color: #5a5a5a;
}

.heart-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 2em;
  color: #9b4dca;
  cursor: pointer;
  transition: color 0.3s ease;
  margin-top: 3px;
  margin-left: 8px;
  line-height: 1;
  vertical-align: middle;
}

.heart-icon:hover {
  color: #f02ac4;
}

.heart-icon:active {
  color: #f02ac4;
}

.arrow-icon {
  margin-left: 8px;
  font-size: 1.2em;
}
</style>