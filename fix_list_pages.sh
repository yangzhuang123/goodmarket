#!/bin/bash

# 修复所有列表页面的空白间隔问题
LIST_PAGES=$(find /Users/young/Desktop/shop/goodmarket/frontend/admin/src/views/modules -name "list.vue")

for file in $LIST_PAGES; do
    echo "修复文件: $file"
    
    # 修复主容器padding
    sed -i '' "s/:style='{\"padding\":\"20px 30px\",\"fontSize\":\"15px\"}'/:style='{\"padding\":\"20px 30px 20px 0\",\"fontSize\":\"15px\"}'/g" "$file"
    
    # 修复el-form margin
    sed -i '' "s/:style='{\"margin\":\"0 10px 20px\"}'/:style='{\"margin\":\"0 0 20px\"}'/g" "$file"
    
    # 修复表格容器padding
    sed -i '' "s/:style='{\"width\":\"100%\",\"padding\":\"0 10px\",\"fontSize\":\"14px\",\"color\":\"#000\"}'/:style='{\"width\":\"100%\",\"padding\":\"0\",\"fontSize\":\"14px\",\"color\":\"#000\"}'/g" "$file"
done

echo "修复完成！"
