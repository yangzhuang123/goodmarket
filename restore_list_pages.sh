#!/bin/bash

# 恢复所有列表页面的原始内边距设置
LIST_PAGES=$(find /Users/young/Desktop/shop/goodmarket/frontend/admin/src/views/modules -name "list.vue")

for file in $LIST_PAGES; do
    echo "恢复文件: $file"
    
    # 恢复主容器padding
    sed -i '' "s/:style='{\"padding\":\"20px 30px 20px 0\",\"fontSize\":\"15px\"}'/:style='{\"padding\":\"20px 30px\",\"fontSize\":\"15px\"}'/g" "$file"
    
    # 恢复el-form margin
    sed -i '' "s/:style='{\"margin\":\"0 0 20px\"}'/:style='{\"margin\":\"0 10px 20px\"}'/g" "$file"
    
    # 恢复表格容器padding
    sed -i '' "s/:style='{\"width\":\"100%\",\"padding\":\"0\",\"fontSize\":\"14px\",\"color\":\"#000\"}'/:style='{\"width\":\"100%\",\"padding\":\"0 10px\",\"fontSize\":\"14px\",\"color\":\"#000\"}'/g" "$file"
done

echo "恢复完成！"
