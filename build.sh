mvn clean package

# build ng and add to jar
cd cwm-ng
yarn install
ng build -prod
bash add_to_war.sh

