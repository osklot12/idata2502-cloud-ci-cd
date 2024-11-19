# build the svelte app
FROM node:18 AS builder

# set the working directory
WORKDIR /app

# copy package.json and package-lock.json files
COPY package*.json ./

# install dependencies
RUN npm install

# copy the rest the app's source code
COPY . .

# inject the environment variable and build the Svelte app for production
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

# serve with nginx
FROM nginx:alpine

# copy the build output to the nginx html folder
COPY --from=builder /app/dist /usr/share/nginx/html

# expose port 80
EXPOSE 80

# start nginx server
CMD ["nginx", "-g", "daemon off;"]