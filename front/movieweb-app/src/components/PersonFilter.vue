<template>
  <el-card class="filter-card" shadow="never">
    <div class="basic-search-section">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Name</label>
          <el-input v-model="filters.personName" placeholder="Search by name or surname" clearable @clear="applyFilters"
            @input="applyFilters" class="name-input">
            <template #append>
              <el-button @click="searchByName" style="width: 50px; height: 36px; padding: 0;">
                <Icon icon="mdi:search" class="search-icon" />
              </el-button>
            </template>
          </el-input>
        </div>
        <div class="filter-item">
          <el-button type="success" @click="clearFilters" class="clear-button">
            Clear filters
            <Icon icon="codicon:clear-all" style="margin-left: 5px; font-size: large; color: #9b4dca;" />
          </el-button>
        </div>
      </div>
    </div>

    <el-collapse v-model="activeNames" class="advanced-search-collapse">
      <el-collapse-item name="1" title="Advanced Search">
        <div class="advanced-search-section">
          <div class="filter-item">
            <label class="filter-label">Country of Origin</label>
            <el-select
  v-model="filters.country"
  filterable
  allow-create
  clearable
  placeholder="Select or enter country"
  @change="applyFilters"
>
  <el-option
    v-for="country in countriesList"
    :key="country"
    :label="country"
    :value="country"
  />
</el-select>

          </div>

          <div class="filter-item">
            <label class="filter-label">Gender</label>
            <div class="gender-checkboxes">
              <el-radio v-model="filters.genders" label="MALE" @change="applyFilters">Male</el-radio>
              <el-radio v-model="filters.genders" label="FEMALE" @change="applyFilters">Female</el-radio>
              <el-radio v-model="filters.genders" label="OTHER" @change="applyFilters">Other</el-radio>
            </div>
          </div>
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
import { ref, computed } from 'vue';
import { Icon } from '@iconify/vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';

const emit = defineEmits(['updateFilters']);

const props = defineProps({
  countries: {
    type: Array,
    default: () => [],
  },
});

const filters = ref({
  personName: '',
  country: null,
  sortByPrice: null,
  genders: [],
});

const countriesList = [
  'United States', 
  'Canada', 
  'United Kingdom', 
  'Australia', 
  'India', 
  'Germany', 
  'France', 
  'Italy', 
  'Spain', 
  'Japan', 
  'South Korea', 
  'Mexico',
];

const sortOptions = [
  { label: 'Worst voted', value: 'averageRatingAsc', icon: ArrowUp },
  { label: 'Best voted', value: 'averageRatingDesc', icon: ArrowDown },
  { label: 'Least votes', value: 'voteCountAsc', icon: ArrowUp },
  { label: 'Most votes', value: 'voteCountDesc', icon: ArrowDown },
];

const applyFilters = () => {
  const filtersToApply = {
    ...filters.value,
    sortByRating: filters.value.sortByPrice,
  };
  
  emit('updateFilters', filtersToApply);
};

const clearFilters = () => {
  filters.value = { personName: '', country: null, sortByPrice: null, genders: [] };
  applyFilters();
};

const activeNames = ref(['0']);
</script>

<style scoped>
.gender-checkboxes {
  display: flex;
  flex-direction: row;
  gap: 10px;
}

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

.role-filters {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.role-filters label {
  font-size: 14px;
  color: #333;
  display: flex;
  align-items: center;
}

.role-filters input[type="checkbox"] {
  margin-right: 8px;
}

.role-filters input[type="checkbox"]:checked {
  background-color: #4caf50;
  border-color: #4caf50;
}
</style>
