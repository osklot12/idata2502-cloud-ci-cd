FROM node:18

# set the working directory
WORKDIR /app

# copy package.json and package-lock.json files
COPY package*.json ./

# install dependencies
RUN npm install

# copy the rest the app's source code
COPY . .

# build the svelte app for production
RUN npm run build

# install 'serve' to serve static files
RUN npm install -g serve

# expose port
EXPOSE 80

# run the app
CMD ["serve", "-s", "dist", "-l", "3000"]