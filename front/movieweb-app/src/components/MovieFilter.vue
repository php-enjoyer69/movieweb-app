<template>
  <el-card class="filter-card" shadow="never">
    <div class="basic-search-section">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Title</label>
          <el-input v-model="filters.title" placeholder="Search movie title" clearable @clear="applyFilters"
            @input="applyFilters" class="title-input">
            <template #append>
              <el-button @click="searchByName" style="width: 50px; height: 36px; padding: 0;">
                <Icon icon="mdi:search" class="search-icon" />
              </el-button>
            </template>
          </el-input>
        </div>
        <div class="filter-item">
          <el-button type="primary" @click="clearFilters" class="clear-button">
            Clear filters <Icon icon="ic:round-clear" style="margin-left: 5px; font-size: large;"/>
          </el-button>
        </div>
      </div>
    </div>

    <el-collapse v-model="activeNames" class="advanced-search-collapse">
      <el-collapse-item name="1" title="Advanced Search">
        <div class="advanced-search-section">
          <div class="filter-year-range">
            <label class="filter-label">Year of production</label>
            <div class="year-range-container">
              <div class="year-input-group">
                <div class="year-label">From</div>
                <el-input v-model="filters.minYear" placeholder="1888" @input="onYearInputChange" class="year-input">
                </el-input>
              </div>
              <div class="year-input-group">
                <div class="year-label">To</div>
                <el-input v-model="filters.maxYear" placeholder="2024" @input="onYearInputChange" class="year-input">
                </el-input>
              </div>
              <el-slider v-model="yearRange" range :min="1888" :max="2025" step="1" @change="updateYearFilter"
                class="year-slider" :marks="marks" />
            </div>
          </div>

          <div class="filter-row">
            <div class="filter-item">
              <label class="filter-label filter-title">Filters</label>
            </div>
          </div>

          <div class="filter-item">
            <label class="filter-label">Genres</label>
            <el-select v-model="filters.genres" multiple filterable :reserve-keyword="false"
              placeholder="Choose movie genre" @change="applyFilters" class="genre-select">
              <el-option v-for="genre in genres" :key="genre.id" :label="genre.name"
                :value="genre.name" />
            </el-select>
          </div>

          <el-divider></el-divider>

        </div>
      </el-collapse-item>
    </el-collapse>

          <div class="filter-row sort-row">
            <div class="filter-item">
              <label class="filter-label">Sort</label>
              <el-select v-model="filters.sortByPrice" placeholder="Choose an option" @change="applyFilters"
                class="sort-select">
                <el-option :label="sortOptions[0].label" :value="sortOptions[0].value">
                  <el-icon class="sort-icon">
                    <ArrowUp />
                  </el-icon> {{ sortOptions[0].label }}
                </el-option>
                <el-option :label="sortOptions[1].label" :value="sortOptions[1].value">
                  <el-icon class="sort-icon">
                    <ArrowDown />
                  </el-icon> {{ sortOptions[1].label }}
                </el-option>
                <el-option :label="sortOptions[2].label" :value="sortOptions[2].value">
                  <el-icon class="sort-icon">
                    <ArrowUp />
                  </el-icon> {{ sortOptions[2].label }}
                </el-option>
                <el-option :label="sortOptions[3].label" :value="sortOptions[3].value">
                  <el-icon class="sort-icon">
                    <ArrowDown />
                  </el-icon> {{ sortOptions[3].label }}
                </el-option>
              </el-select>
            </div>
          </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import { Icon } from '@iconify/vue';

const props = defineProps({
  genres: {
    type: Array,
    default: () => [],
  },
});

const filters = ref({
  title: '',
  minYear: null,
  maxYear: null,
  sortByRating: null,
  sortByYear: null,
  genres: [],
});

const sortOptions = [
  { label: 'Worst rated', value: 'ratingAsc', icon: ArrowUp },
  { label: 'Best rated', value: 'ratingDesc', icon: ArrowDown },
  { label: 'Oldest', value: 'yearAsc', icon: ArrowUp },
  { label: 'Newest', value: 'yearDesc', icon: ArrowDown },
];

const emit = defineEmits(['updateFilters']);

const applyFilters = () => {
  emit('updateFilters', filters.value);
};

const yearRange = ref([1888, 2024]);

const marks = {
  1888: '1888',
  2025: '2025',
};

const clearFilters = () => {
  filters.value = { title: '', minYear: null, maxYear: null, sortByRating: null, sortByYear: null, genres: [] };
  yearRange.value = [1888, 2025];
  applyFilters();
};

const onYearInputChange = () => {
  if (filters.value.minYear > filters.value.maxYear && filters.value.maxYear !== null) {
    filters.value.minYear = filters.value.maxYear;
  }
  if (filters.value.minYear < 1888) filters.value.minYear  = 1888;
  if (filters.value.maxYear  > 2025) filters.value.maxYear  = 2025;
  if (filters.value.minYear !== null && filters.value.maxYear  === null) {
    filters.value.maxYear  = 2025;
  }
  yearRange.value = [filters.value.minYear , filters.value.maxYear];
  applyFilters();
};

const updateYearFilter = () => {
  if (yearRange.value[0] > yearRange.value[1]) {
    filters.value.minYear  = yearRange.value[1];
    filters.value.maxYear  = yearRange.value[0];
  } else {
    filters.value.minYear  = yearRange.value[0];
    filters.value.maxYear  = yearRange.value[1];
  }

  applyFilters();
};

const activeNames = ref(['0']);
</script>

<style scoped>
.filter-card {
  max-width: 85%;
  margin: 0 auto 20px;
  margin-bottom: 10px;
  text-align: left;
  background-color: #f4f4f4;
}

.basic-search-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
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
  width: 100%;
  max-width: 375px;
}

.advanced-search-collapse {
  margin-top: 20px;
  
}

.advanced-search-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: #f4f4f4;
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

.quantity-select,
.sort-select {
  width: 100%;
  font-family: Quicksand;
  font-weight: 500;
}

.genre-select {
  width: 480px;
  font-family: Quicksand;
  font-weight: 500;
}

.year-slider {
  width: 100%;
  margin-top: 20px;
  margin-left: 10px;
  margin-right: 300px;
}

.sort-icon {
  margin-right: 8px;
}

.clear-button {
  max-width: 180px;
  align-self: left;
  margin-top: 24px;
}

.el-divider {
  margin-top: 42px;
  margin-bottom: 15px;
}

.search-icon {
  font-size: 18px;
}

.sort-row {
  margin-top: 40px;
  margin-left: 74.5%;
  justify-content: flex-end;
  align-items: end;
  width: 25.5%;
  gap: 20px;
}

.year-input ::placeholder {
  width: 80px;
  font-family: Quicksand;
  font-weight: 500;
}

.year-range-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.year-input-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
  width: 60px;
}

.year-input {
  font-family: Quicksand;
}

.year-label {
  font-size: 12px;
  color: #757575;
}

.year-slider {
  flex-grow: 1;
  width: 300px;
}

.filter-year-range {
  margin-top: 10px;
}
</style>
