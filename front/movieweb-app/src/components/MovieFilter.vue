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
          <el-button type="success" @click="clearFilters" class="clear-button">
            Clear filters <Icon icon=codicon:clear-all style="margin-left: 5px; font-size: large; color: #9b4dca;"/>
          </el-button>
        </div>
      </div>
    </div>

    <el-collapse v-model="activeNames" class="advanced-search-collapse">
      <el-collapse-item name="1" title="Advanced Search">
        <div class="advanced-search-section">
          <!-- <div class="filter-item">
            <label class="filter-label">Person</label>
            <el-input
    v-model="filters.personName"
    placeholder="Enter person's name or surname"
    clearable
    @input="applyFilters"
    class="person-input"
  />
</div> -->
          <div class="filter-year-range">
            <label class="filter-label">Year of premiere</label>
            <div class="year-range-container">
              <div class="year-input-group">
                <div class="year-label">From</div>
                <el-input v-model="filters.minYear" placeholder="1900" @input="onYearInputChange" class="year-input">
                </el-input>
              </div>
              <div class="year-input-group">
                <div class="year-label">To</div>
                <el-input v-model="filters.maxYear" placeholder="2025" @input="onYearInputChange" class="year-input">
                </el-input>
              </div>
              <el-slider v-model="yearRange" range :min="1900" :max="2025" step="1" @change="updateYearFilter"
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
                <el-option :label="sortOptions[4].label" :value="sortOptions[4].value">
                  <el-icon class="sort-icon">
                    <ArrowDown />
                  </el-icon> {{ sortOptions[4].label }}
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
  personName: '',
});

const sortOptions = [
  { label: 'Worst rated', value: 'averageRatingAsc', icon: ArrowUp },
  { label: 'Best rated', value: 'averageRatingDesc', icon: ArrowDown },
  { label: 'Oldest', value: 'yearAsc', icon: ArrowUp },
  { label: 'Newest', value: 'yearDesc', icon: ArrowDown },
  { label: 'Most rated', value: 'ratingCountDesc', icon: ArrowDown },
];

const emit = defineEmits(['updateFilters']);

const applyFilters = () => {
  emit('updateFilters', {
    ...filters.value,
    sortByRating: filters.value.sortByPrice === 'averageRatingAsc' || filters.value.sortByPrice === 'averageRatingDesc' ? filters.value.sortByPrice : null,
    sortByYear: filters.value.sortByPrice === 'yearAsc' || filters.value.sortByPrice === 'yearDesc' ? filters.value.sortByPrice : null,
    sortByPopularity: filters.value.sortByPrice === 'ratingCountDesc' ? filters.value.sortByPrice : null,
    person: filters.value.personName,
  });
};

const yearRange = ref([1900, 2025]);

const marks = {
  1900: '1900',
  2025: '2025',
};

const clearFilters = () => {
  filters.value = { title: '', minYear: null, maxYear: null, sortByRating: null, sortByYear: null, sortByPopularity: null, genres: [], person: '' };
  yearRange.value = [1900, 2025];
  applyFilters();
};

const onYearInputChange = () => {
  if (filters.value.minYear > filters.value.maxYear && filters.value.maxYear !== null) {
    filters.value.minYear = filters.value.maxYear;
  }
  if (filters.value.minYear < 1900) filters.value.minYear  = 1900;
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
  max-width: 160px;
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
