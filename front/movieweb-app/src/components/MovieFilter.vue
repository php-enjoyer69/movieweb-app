<template>
    <el-card class="filter-container" shadow="never">
      <div class="filters">
        <div class="filter-row" >
          <el-input
            v-model="filters.title"
            placeholder="Search the title"
            clearable
            @clear="applyFilters"
            @input="applyFilters"
            class="title-filter"
          /> 
          <el-button type="primary" @click="clearFilters">Clear filters</el-button>
        </div>
      </div>
    </el-card>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const filters = ref({
    title: '',
  });
  
  const emit = defineEmits(['updateFilters']);
  
  const applyFilters = () => {
    emit('updateFilters', filters.value);
  };
  
  const clearFilters = () => {
    filters.value = { title: ''};
    applyFilters();
  };
  
  </script>
  
  <style scoped>
  .filter-container {
    max-width: 85%;
    margin: 0 auto 20px;
    margin-bottom: -10px;
    text-align: left;
    background-color: #f4f4f4;
  }
  
  .filters {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    justify-content: center;
  }
  
  .filter-row {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    width: 100%;
  }
  
  .title-filter {
    flex: 2;
    max-width: 250px;
    font-family: 'Phudu', sans-serif !important;
  }
  
  </style>