mvn clean package

# build ng and add to jar
cd cwm-ng
# yarn install
# npm install
ng build
bash add_to_war.sh

