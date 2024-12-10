<template>
    <div class="pagination">
      <el-pagination
        layout="prev, pager, next"
        :total="totalElements"
        :page-size="itemsPerPage"
        :current-page="currentPage"
        @current-change="onPageChange"
        background
      />
    </div>
  </template>
  
  <script setup>
  import { defineProps, defineEmits } from 'vue';
  
  const props = defineProps({
    currentPage: {
      type: Number,
      required: true,
    },
    totalElements: {
      type: Number,
      required: true,
    },
    itemsPerPage: {
      type: Number,
      required: true,
    },
  });
  
  const emit = defineEmits();
  
  const onPageChange = (page) => {
    emit('page-change', page); 
  };
  </script>
  
  <style scoped>
  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  </style>
  