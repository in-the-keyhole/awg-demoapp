@import com.awginc.storemaster.web.IndexModel
@param IndexModel model

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>AWG Store Master</title>
        <base href="${model.basePath()}" />

        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" type="image/x-icon" href="favicon.png" />
        <link rel="stylesheet" crossorigin href="assets/index.css?v=${model.version()}">
        <script type="module" crossorigin src="assets/index.js?v=${model.version()}"></script>
    </head>

    <body>
        <div id="root"></div>
    </body>

</html>
