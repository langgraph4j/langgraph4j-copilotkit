cd ag-ui
git fetch origin
git checkout main   # or the correct branch
git pull

git add ag-ui
git commit -m "Update ag-ui submodule reference"
git push

#git submodule update --init --remote