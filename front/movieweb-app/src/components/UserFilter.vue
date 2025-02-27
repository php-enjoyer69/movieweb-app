<template>
    <el-card class="filter-card" shadow="never">
      <div class="basic-search-section">
        <div class="filter-row">
          <div class="filter-item">
            <label class="filter-label">Username</label>
            <el-input v-model="filters.name" placeholder="Search by username" clearable @clear="applyFilters"
              @input="applyFilters" class="title-input">
              <template #append>
                <el-button @click="searchByName" style="width: 50px; height: 36px; padding: 0;">
                  <Icon icon="mdi:search" class="search-icon" />
                </el-button>
              </template>
            </el-input>
          </div>
          <div class="filter-item">
            <el-button type="success" @click="clearFilters" class="clear-button">
              Clear search
              <Icon icon=codicon:clear-all style="margin-left: 5px; font-size: large; color: #9b4dca;" />
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import { Icon } from '@iconify/vue';

const filters = ref({
  name: ''
});

const emit = defineEmits(['updateFilters']);

const applyFilters = () => {
  emit('updateFilters', filters.value); 
};

const clearFilters = () => {
  filters.value = { name: '' };
  applyFilters();
};

const searchByName = () => {
  applyFilters(); 
};

const activeNames = ref(['0']);
</script>

<style scoped>
.filter-card {
  max-width: 95%;
  margin: 0 auto 20px;
  margin-bottom: 10px;
  text-align: left;
  background-color: #f8f8f8;
}

.basic-search-section {
  display: flex;
  flex-direction: column;
}

.filter-row {
  display: flex;
  gap: 20px;
  width: 100%;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  flex-direction: column;
  align-items: left;
  width: 90%;
  max-width: 280px;
}

.filter-label {
  font-size: 12px;
  font-weight: normal;
  margin-bottom: 8px;
  color: #666;
  text-align: left;
}

.filter-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: -5px;
}

.title-input ::placeholder {
  font-family: Quicksand !important;
  font-weight: 500;
}

.clear-button {
  max-width: 160px;
  align-self: left;
  margin-top: 24px;
}

.search-icon {
  font-size: 18px;
}
</style>